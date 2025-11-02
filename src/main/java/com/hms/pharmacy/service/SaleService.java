
package com.hms.pharmacy.service;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.exception.HmsException;

public interface SaleService {
    Long createSale(SaleDTO saleDTO) throws HmsException;

    void updateSale(SaleDTO saleDTO) throws HmsException;

    SaleDTO getSaleById(Long id) throws HmsException;

    SaleDTO getSaleByPrescriptionId(Long prescriptionId) throws HmsException;
}
