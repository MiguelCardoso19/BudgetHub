package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface SupplierService {
    SupplierDTO create(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException;
    SupplierDTO update(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException;
    void delete(UUID id);
    SupplierDTO getById(UUID id) throws ExecutionException, InterruptedException, GenericException;
}