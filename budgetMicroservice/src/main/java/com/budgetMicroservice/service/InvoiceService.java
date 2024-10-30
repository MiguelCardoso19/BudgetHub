package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.exception.FailedToUploadFileException;
import com.budgetMicroservice.exception.InvoiceAlreadyExistsException;
import com.budgetMicroservice.exception.InvoiceNotFoundException;
import com.budgetMicroservice.exception.MovementNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface InvoiceService {
    InvoiceDTO attachFileToInvoice(UUID invoiceId, MultipartFile file) throws InvoiceNotFoundException, FailedToUploadFileException;
    InvoiceDTO create(InvoiceDTO invoiceDTO) throws InvoiceAlreadyExistsException, MovementNotFoundException;
    InvoiceDTO update(InvoiceDTO invoiceDTO) throws InvoiceNotFoundException, InvoiceAlreadyExistsException;
    void delete(UUID id) throws InvoiceNotFoundException;
    Page<InvoiceDTO> getAll(Pageable pageable);
    InvoiceDTO getById(UUID id) throws InvoiceNotFoundException;
    InvoiceDTO addMovementToInvoice(UUID invoiceId, UUID movementId) throws InvoiceNotFoundException, MovementNotFoundException;
}
