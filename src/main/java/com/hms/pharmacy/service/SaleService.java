package com.hms.pharmacy.service;

import java.util.List;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.dto.SaleRequest;
import com.hms.pharmacy.exception.HmsException;

public interface SaleService {
    Long createSale(SaleRequest saleRequest) throws HmsException;

    void updateSale(SaleDTO saleDTO) throws HmsException;

    SaleDTO getSaleById(Long id) throws HmsException;

    SaleDTO getSaleByPrescriptionId(Long prescriptionId) throws HmsException;

    List<SaleDTO> getAllSales() throws HmsException;
}
