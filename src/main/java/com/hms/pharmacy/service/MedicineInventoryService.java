package com.hms.pharmacy.service;

import java.util.List;

import com.hms.pharmacy.dto.MedicineInventoryDTO;
import com.hms.pharmacy.exception.HmsException;

public interface MedicineInventoryService {
    public MedicineInventoryDTO addMedicine(MedicineInventoryDTO medicineInventoryDTO) throws HmsException;

    public MedicineInventoryDTO getMedicineById(Long id) throws HmsException;

    public MedicineInventoryDTO updateMedicine(MedicineInventoryDTO medicineDTO) throws HmsException;

    public List<MedicineInventoryDTO> getAllMedicines() throws HmsException;

    public void deleteMedicine(Long id) throws HmsException;

}
