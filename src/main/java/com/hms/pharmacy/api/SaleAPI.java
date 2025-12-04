package com.hms.pharmacy.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.pharmacy.dto.ResponseDTO;
import com.hms.pharmacy.dto.SaleDTO;
import com.hms.pharmacy.dto.SaleItemDTO;
import com.hms.pharmacy.dto.SaleRequest;
import com.hms.pharmacy.exception.HmsException;
import com.hms.pharmacy.service.SaleItemService;
import com.hms.pharmacy.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pharmacy/sales")
@Validated
@CrossOrigin
@RequiredArgsConstructor
public class SaleAPI {

    private final SaleService saleService;
    private final SaleItemService saleItemService;

    @PostMapping("/create")
    public ResponseEntity<Long> createSale(@RequestBody SaleRequest saleRequest) throws HmsException {
        return new ResponseEntity<>(saleService.createSale(saleRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateSale(@RequestBody SaleDTO saleDTO) throws HmsException {
        saleService.updateSale(saleDTO);
        return new ResponseEntity<>(new ResponseDTO("Sale Updated successfully"), HttpStatus.OK);
    }

    @GetMapping("/getSaleItems/{saleId}")
    public ResponseEntity<List<SaleItemDTO>> getSaleItemsBySaleId(@PathVariable Long saleId) throws HmsException {
        List<SaleItemDTO> saleItems = saleItemService.getSaleItemsBySaleId(saleId);
        return new ResponseEntity<>(saleItems, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SaleItemDTO> getSaleItemById(@PathVariable Long saleId) throws HmsException {
        return new ResponseEntity<>(saleItemService.getSaleItemById(saleId), HttpStatus.OK);
    }




    



}
