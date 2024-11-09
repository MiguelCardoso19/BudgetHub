package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface SupplierService {
    SupplierDTO create(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    SupplierDTO update(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    void delete(UUID id);
    SupplierDTO getById(UUID id) throws ExecutionException, InterruptedException, GenericException, TimeoutException;
    CustomPageDTO getAll(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
}