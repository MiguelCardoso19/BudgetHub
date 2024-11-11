package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface InvoiceService {
    InvoiceDTO create(InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    InvoiceDTO update(InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    void delete(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    InvoiceDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    void attachBase64FileToInvoice(UUID invoiceId, MultipartFile file) throws FailedToUploadFileException, ExecutionException, InterruptedException, TimeoutException;
    CustomPageDTO getAll(Pageable pageable) throws GenericException, ExecutionException, InterruptedException, TimeoutException;
    CompletableFuture<InvoiceDTO> getPendingRequest(UUID correlationId, UUID id);
    CompletableFuture<CustomPageDTO> getPendingPageRequest(UUID correlationId);
}
