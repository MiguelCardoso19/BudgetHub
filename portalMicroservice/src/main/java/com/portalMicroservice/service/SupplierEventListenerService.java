package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;

public interface SupplierEventListenerService {
    void handleInvoiceResponse(SupplierDTO supplierDTO) throws GenericException;
    void handleInvoicePageResponse(CustomPageDTO customPageDTO) throws GenericException;
}
