package com.hms.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
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
        if (existingInventory.getQuantity() < medicineInventoryDTO.getQuantity()) {
            medicineService.addStock(medicineInventoryDTO.getMedicineId(),
                    medicineInventoryDTO.getQuantity() - existingInventory.getQuantity());
        } else if (existingInventory.getQuantity() > medicineInventoryDTO.getQuantity()) {
            medicineService.removeStock(medicineInventoryDTO.getMedicineId(),
                    existingInventory.getQuantity() - medicineInventoryDTO.getQuantity());
        }
        existingInventory.setQuantity(medicineInventoryDTO.getQuantity());
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

}
