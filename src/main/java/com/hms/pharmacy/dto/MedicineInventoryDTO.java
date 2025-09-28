package com.hms.pharmacy.dto;

import java.time.LocalDate;

import com.hms.pharmacy.entity.Medicine;
import com.hms.pharmacy.entity.MedicineInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineInventoryDTO {
    private Long id;
    private Long medicineId;
    private String batchNo;
    private Integer quantity;
    private LocalDate expiryDate;
    private LocalDate addedDate;

    public MedicineInventory toEntity() {
        return new MedicineInventory(
                this.id,
                new Medicine(this.medicineId),
                this.batchNo,
                this.quantity,
                this.expiryDate,
                this.addedDate);
    }

}
