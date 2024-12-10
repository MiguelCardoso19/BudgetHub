package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.budget.SupplierNotFoundException;
import com.portalMicroservice.exception.budget.SupplierValidationException;

import java.util.UUID;

public interface SupplierEventListenerService {
    void handleInvoiceResponse(SupplierDTO supplierDTO);
    void handleInvoicePageResponse(CustomPageDTO customPageDTO);
    void handleDeleteSuccess(UUID id);
    void handleNotFoundExceptionResponse(SupplierNotFoundException errorPayload);
    void handleValidationExceptionResponse(SupplierValidationException errorPayload);
}
