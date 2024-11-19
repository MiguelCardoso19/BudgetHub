package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.AttachFileRequestDTO;
import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.model.Invoice;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface InvoiceService {
    void attachMultipartFileToInvoice(UUID id, MultipartFile file) throws InvoiceNotFoundException, FailedToUploadFileException;
    InvoiceDTO create(InvoiceDTO invoiceDTO) throws InvoiceAlreadyExistsException, MovementNotFoundException, DocumentNumberNotFoundException;
    InvoiceDTO update(InvoiceDTO invoiceDTO) throws InvoiceNotFoundException, InvoiceAlreadyExistsException;
    void delete(UUID id) throws InvoiceNotFoundException;
    Page<InvoiceDTO> getAll(CustomPageableDTO customPageableDTO) throws JsonProcessingException;
    InvoiceDTO findInvoiceDTOById(UUID id) throws InvoiceNotFoundException;
    Invoice findInvoiceEntityById(UUID id) throws InvoiceNotFoundException;
    void attachBase64FileToInvoice(AttachFileRequestDTO attachFileRequestDTO) throws InvoiceNotFoundException, FailedToUploadFileException;
    boolean existsById(UUID invoiceId);
}
