package com.hms.pharmacy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hms.pharmacy.dto.StockStatus;
import com.hms.pharmacy.entity.MedicineInventory;

public interface MedicineInventoryRepository extends CrudRepository<MedicineInventory, Long> {

    List<MedicineInventory> findByExpiryDateBefore(LocalDate date);

    // select * from medicine_inventory where medicine_id = ? and expiry_date > ? and quantity > ? and status != ? order by expiry_date asc
    List<MedicineInventory> findByMedicineIdAndExpiryDateAfterAndQuantityGreaterThanAndStatusOrderByExpiryDateAsc(Long medicineId, LocalDate date, Integer quantity, StockStatus status);

}
