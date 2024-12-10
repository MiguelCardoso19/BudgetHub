package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.exception.budget.InvoiceNotFoundException;
import com.portalMicroservice.service.InvoiceEventListenerService;
import com.portalMicroservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class InvoiceEventListenerServiceImpl implements InvoiceEventListenerService {
    private final InvoiceService invoiceService;

    @Override
    @KafkaListener(topics = "invoice-response", groupId = "invoice_response_group", concurrency = "10", containerFactory = "invoiceKafkaListenerContainerFactory")
    public void handleInvoiceResponse(InvoiceDTO invoiceDTO) {
        CompletableFuture<InvoiceDTO> future = invoiceService.removePendingRequestById(invoiceDTO.getCorrelationId(), invoiceDTO.getId());
        if (future != null) {
            future.complete(invoiceDTO);
        }
    }

    @Override
    @KafkaListener(topics = "invoice-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleInvoicePageResponse(CustomPageDTO customPageDTO) {
        CompletableFuture<CustomPageDTO> future = invoiceService.removePendingPageRequestById(customPageDTO.getPageable().getCorrelationId());
        if (future != null) {
            future.complete(customPageDTO);
        }
    }

    @Override
    @KafkaListener(topics = "invoice-delete-success-response", groupId = "invoice_delete_success_response_group", concurrency = "10")
    public void handleDeleteSuccess(UUID id) {
        CompletableFuture<InvoiceDTO> future = invoiceService.removePendingRequestById(id, null);
        if (future != null) {
            future.complete(null);
        }
    }

    @Override
    @KafkaListener(topics = "invoice-not-found-exception-response", groupId = "invoice_not_found_response_group", concurrency = "10", containerFactory = "invoiceNotFoundExceptionKafkaListenerContainerFactory")
    public void handleNotFoundExceptionResponse(InvoiceNotFoundException errorPayload) {
        CompletableFuture<InvoiceDTO> future = invoiceService.removePendingRequestById(UUID.fromString(errorPayload.getId()), null);
        if (future != null) {
            future.completeExceptionally(new InvoiceNotFoundException(errorPayload.getId()));
        }
    }

    @Override
    @KafkaListener(topics = "upload-file-success-response", groupId = "upload_file_success_response_group", concurrency = "10")
    public void handleUploadFileSuccess(UUID id) {
        CompletableFuture<InvoiceDTO> future = invoiceService.removePendingRequestById(id, null);
        if (future != null) {
            future.complete(null);
        }
    }

    @Override
    @KafkaListener(topics = "failed-to-upload-file-exception-response", groupId = "failed_to_upload_file_exception_group", concurrency = "10", containerFactory = "failedToUploadFileExceptionKafkaListenerContainerFactory")
    public void handleFailedToUploadFileExceptionResponse(FailedToUploadFileException errorPayload) {
        CompletableFuture<InvoiceDTO> future = invoiceService.removePendingRequestById(errorPayload.getId(), null);
        if (future != null) {
            future.completeExceptionally(new FailedToUploadFileException(errorPayload.getId()));
        }
    }
}