package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.SupplierNotFoundException;
import com.portalMicroservice.exception.budget.SupplierValidationException;
import com.portalMicroservice.service.SupplierEventListenerService;
import com.portalMicroservice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class SupplierEventListenerServiceImpl implements SupplierEventListenerService {
    private final SupplierService supplierService;

    @Override
    @KafkaListener(topics = "supplier-response", groupId = "supplier_response_group", concurrency = "10", containerFactory = "supplierKafkaListenerContainerFactory")
    public void handleInvoiceResponse(SupplierDTO supplierDTO) throws GenericException {
        CompletableFuture<SupplierDTO> future = supplierService.removePendingRequestById(supplierDTO.getCorrelationId(), supplierDTO.getId());

        if (future != null) {
            future.complete(supplierDTO);
        } else {
            throw new GenericException();
        }
    }

    @Override
    @KafkaListener(topics = "page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleInvoicePageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = supplierService.removePendingPageRequestById(customPageDTO.getPageable().getCorrelationId());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }

    @Override
    @KafkaListener(topics = "supplier-delete-success-response", groupId = "supplier_delete_success_response_group", concurrency = "10")
    public void handleDeleteSuccess(UUID id) throws GenericException {
        CompletableFuture<SupplierDTO> future = supplierService.removePendingRequestById(id, null);

        if (future != null) {
            future.complete(null);
        } else {
            throw new GenericException();
        }
    }

    @Override
    @KafkaListener(topics = "supplier-not-found-exception-response", groupId = "supplier_not_found_response_group", concurrency = "10", containerFactory = "supplierNotFoundExceptionKafkaListenerContainerFactory")
    public void handleNotFoundExceptionResponse(SupplierNotFoundException errorPayload) {
        CompletableFuture<SupplierDTO> future = supplierService.removePendingRequestById(UUID.fromString(errorPayload.getId()), null);

        if (future != null) {
            future.completeExceptionally(new SupplierNotFoundException(errorPayload.getId()));
        }
    }

    @Override
    @KafkaListener(topics = "supplier-validation-exception-response", groupId = "supplier_validation_response_group", concurrency = "10", containerFactory = "supplierValidationExceptionKafkaListenerContainerFactory")
    public void handleValidationExceptionResponse(SupplierValidationException errorPayload) {
        CompletableFuture<SupplierDTO> future = supplierService.removePendingRequestById(errorPayload.getId(), null);

        if (future != null) {
            String formatedErrorMessage = errorPayload.getMessage().substring(errorPayload.getMessage().indexOf("[") + 1, errorPayload.getMessage().indexOf("]"));
            future.completeExceptionally(new SupplierValidationException(formatedErrorMessage));
        }
    }
}
