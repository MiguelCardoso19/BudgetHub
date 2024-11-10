package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;

public interface InvoiceEventListenerService {
    void handleInvoiceResponse(InvoiceDTO invoiceDTO) throws GenericException;
    void handleInvoicePageResponse(CustomPageDTO customPageDTO) throws GenericException;
}
