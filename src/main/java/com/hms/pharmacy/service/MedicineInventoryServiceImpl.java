package com.hms.pharmacy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
import com.hms.pharmacy.dto.StockStatus;
import com.hms.pharmacy.entity.Medicine;
import com.hms.pharmacy.entity.MedicineInventory;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.MedicineInventoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicineInventoryServiceImpl implements MedicineInventoryService {
    private final MedicineInventoryRepository medicineInventoryRepository;

    private final MedicineService medicineService;

    @Override
    public MedicineInventoryDTO addMedicine(MedicineInventoryDTO medicineInventoryDTO) throws HmsException {
        medicineInventoryDTO.setAddedDate(LocalDateTime.now().toLocalDate());

        medicineService.addStock(medicineInventoryDTO.getMedicineId(), medicineInventoryDTO.getQuantity());

        medicineInventoryDTO.setInitialQuantity(medicineInventoryDTO.getQuantity());

        medicineInventoryDTO.setStatus(StockStatus.ACTIVE);

        return medicineInventoryRepository.save(medicineInventoryDTO.toEntity()).toDTO();
    }

    @Override
    public MedicineInventoryDTO getMedicineById(Long id) throws HmsException {
        return medicineInventoryRepository.findById(id)
                .orElseThrow(() -> new HmsException("INVENTORY_NOT_FOUND")).toDTO();
    }

    @Override
    public MedicineInventoryDTO updateMedicine(MedicineInventoryDTO medicineInventoryDTO) throws HmsException {
        MedicineInventory existingInventory = medicineInventoryRepository.findById(medicineInventoryDTO.getId())
                .orElseThrow(() -> new HmsException("INVENTORY_NOT_FOUND"));

        existingInventory.setBatchNo(medicineInventoryDTO.getBatchNo());
        if (existingInventory.getInitialQuantity() < medicineInventoryDTO.getQuantity()) {
            medicineService.addStock(medicineInventoryDTO.getMedicineId(),
                    medicineInventoryDTO.getQuantity() - existingInventory.getInitialQuantity());
        } else if (existingInventory.getInitialQuantity() > medicineInventoryDTO.getQuantity()) {
            medicineService.removeStock(medicineInventoryDTO.getMedicineId(),
                    existingInventory.getInitialQuantity() - medicineInventoryDTO.getQuantity());
        }
        existingInventory.setQuantity(medicineInventoryDTO.getQuantity());
        existingInventory.setInitialQuantity(medicineInventoryDTO.getQuantity());
        existingInventory.setExpiryDate(medicineInventoryDTO.getExpiryDate());

        return medicineInventoryRepository.save(existingInventory).toDTO();

    }

    @Override
    public List<MedicineInventoryDTO> getAllMedicines() throws HmsException {
        return ((List<MedicineInventory>) medicineInventoryRepository.findAll()).stream().map(MedicineInventory::toDTO)
                .toList();
    }

    @Override
    public void deleteMedicine(Long id) throws HmsException {
        medicineInventoryRepository.deleteById(id);
    }

    private void markExpired(List<MedicineInventory> inventories) throws HmsException {
        for (MedicineInventory inventory : inventories) {
            inventory.setStatus(StockStatus.EXPIRED);
        }
        medicineInventoryRepository.saveAll(inventories);
    }

    // "0 30 14 * * ?"
    /* seconds minutes hours dayofMonth month dayofweek */
    @Scheduled(cron = "30 41 1 * * ?")
    @Override
    public void deleteExpiredMedicines() throws HmsException {
        System.out.println("Deleting expired medicines...");
        List<MedicineInventory> expiredMidicines = medicineInventoryRepository.findByExpiryDateBefore(LocalDate.now());

        for (MedicineInventory medicine : expiredMidicines) {
            medicineService.removeStock(medicine.getMedicine().getId(), medicine.getQuantity());
        }

        this.markExpired(expiredMidicines);
    }

}
