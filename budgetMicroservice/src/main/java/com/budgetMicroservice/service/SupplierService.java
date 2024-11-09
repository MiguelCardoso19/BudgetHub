package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.exception.SupplierValidationException;
import com.budgetMicroservice.model.Supplier;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SupplierService {
    Supplier findSupplierEntityById(UUID id) throws SupplierNotFoundException;
    SupplierDTO create(SupplierDTO supplierDTO) throws SupplierValidationException;
    void delete(UUID id) throws SupplierNotFoundException;
    SupplierDTO update(SupplierDTO supplierDTO) throws SupplierNotFoundException, SupplierValidationException;
    Page<SupplierDTO> findAll(CustomPageableDTO customPageableDTO) throws JsonProcessingException;
    SupplierDTO findSupplierDTOById(UUID id) throws SupplierNotFoundException;
    boolean existsById(UUID supplierId);
    }