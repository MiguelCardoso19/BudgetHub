package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.exception.budget.InvoiceNotFoundException;

import java.util.UUID;

public interface InvoiceEventListenerService {
    void handleInvoiceResponse(InvoiceDTO invoiceDTO) throws GenericException;
    void handleInvoicePageResponse(CustomPageDTO customPageDTO) throws GenericException;
    void handleDeleteSuccess(UUID id) throws GenericException;
    void handleNotFoundExceptionResponse(InvoiceNotFoundException errorPayload);
    void handleUploadFileSuccess(UUID id) throws GenericException;
    void handleFailedToUploadFileExceptionResponse(FailedToUploadFileException errorPayload);
}
