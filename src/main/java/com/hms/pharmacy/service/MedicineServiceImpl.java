package com.hms.pharmacy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.MedicineDTO;
import com.hms.pharmacy.entity.Medicine;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;

    @Override
    public Long addMedicine(MedicineDTO medicineDTO) throws HmsException {
        Optional<Medicine> existingMedicine = medicineRepository
                .findByNameIgnoreCaseAndDosageIgnoreCase(medicineDTO.getName(), medicineDTO.getDosage());

        if (existingMedicine.isPresent()) {
            throw new HmsException("MEDICINE_ALREADY_EXISTS");
        }

        medicineDTO.setCreatedAt(LocalDateTime.now());

        return medicineRepository.save(medicineDTO.toEntity()).getId();
    }

    @Override
    public MedicineDTO getMedicineById(Long id) throws HmsException {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new HmsException("MEDICINE_NOT_FOUND")).toDTO();
    }

    @Override
    public void updateMedicine(MedicineDTO medicineDTO) throws HmsException {
        Medicine existingMedicine = medicineRepository.findById(medicineDTO.getId())
                .orElseThrow(() -> new HmsException("MEDICINE_NOT_FOUND"));

        if (!(medicineDTO.getName().equalsIgnoreCase(existingMedicine.getName())
                && medicineDTO.getDosage().equalsIgnoreCase(existingMedicine.getDosage()))) {
            /* Check if a medicine with the same name and dosage already exists */
            Optional<Medicine> medicineWithSameNameAndDosage = medicineRepository
                    .findByNameIgnoreCaseAndDosageIgnoreCase(medicineDTO.getName(), medicineDTO.getDosage());
            if (medicineWithSameNameAndDosage.isPresent()) {
                throw new HmsException("MEDICINE_ALREADY_EXISTS");
            }
        }

        existingMedicine.setName(medicineDTO.getName());
        existingMedicine.setDosage(medicineDTO.getDosage());
        existingMedicine.setCategory(medicineDTO.getCategory());
        existingMedicine.setType(medicineDTO.getType());
        existingMedicine.setManufacturer(medicineDTO.getManufacturer());
        existingMedicine.setUnitPrice(medicineDTO.getUnitPrice());
        medicineRepository.save(existingMedicine);
    }

    @Override
    public List<MedicineDTO> getAllMedicines() throws HmsException {
        return ((List<Medicine>) medicineRepository.findAll()).stream().map(Medicine::toDTO).toList();
    }

}
