package com.hms.pharmacy.entity;

import java.time.LocalDate;

import com.hms.pharmacy.dto.MedicineInventoryDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MedicineInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "madicine_id", nullable = false)
    private Medicine medicine;
    private String batchNo;
    private Integer quantity;
    private LocalDate expiryDate;
    private LocalDate addedDate;

    public MedicineInventory(Long id) {
        this.id = id;
    }

    public MedicineInventoryDTO toDTO() {
        return new MedicineInventoryDTO(
                this.id,
                this.medicine != null ? this.medicine.getId() : null,
                this.batchNo,
                this.quantity,
                this.expiryDate,
                this.addedDate);
    }

}
