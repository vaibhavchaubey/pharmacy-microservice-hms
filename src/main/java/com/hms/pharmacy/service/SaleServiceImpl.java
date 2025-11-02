package com.hms.pharmacy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.entity.Sale;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;


    @Override
    public Long createSale(SaleDTO saleDTO) throws HmsException {
        if (saleRepository.existsByPrescriptionId(saleDTO.getPrescriptionId())) {
            throw new HmsException("SALE_ALREADY_FOUND");

        }

        saleDTO.setSaleDate(LocalDateTime.now());
        return saleRepository.save(saleDTO.toEntity()).getId();
    }

    @Override
    public void updateSale(SaleDTO saleDTO) throws HmsException {
        Sale sale = saleRepository.findById(saleDTO.getId())
                .orElseThrow(() -> new HmsException("SALE_NOT_FOUND"));

        sale.setSaleDate(saleDTO.getSaleDate());
        sale.setTotalAmount(saleDTO.getTotalAmount());
        saleRepository.save(sale);
    }

    @Override
    public SaleDTO getSaleById(Long id) throws HmsException {
        return saleRepository.findById(id)
                .orElseThrow(() -> new HmsException("SALE_NOT_FOUND"))
                .toDTO();
    }

    @Override
    public SaleDTO getSaleByPrescriptionId(Long prescriptionId) throws HmsException {
        return saleRepository.findByPrescriptionId(prescriptionId)
                .orElseThrow(() -> new HmsException("SALE_NOT_FOUND"))
                .toDTO();
    }

}
