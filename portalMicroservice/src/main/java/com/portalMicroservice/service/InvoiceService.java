package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.AttachFileRequestDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;

import java.util.UUID;

public interface InvoiceService {
    InvoiceDTO create(InvoiceDTO invoiceDTO) throws GenericException;
    InvoiceDTO update(InvoiceDTO invoiceDTO) throws GenericException;
    void delete(UUID id) throws GenericException;
    InvoiceDTO getById(UUID id) throws GenericException;
    void attachBase64FileToInvoice(AttachFileRequestDTO attachFileRequestDTO);
}
