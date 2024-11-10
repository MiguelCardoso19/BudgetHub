package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.SupplierEventListenerService;
import com.portalMicroservice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class SupplierEventListenerServiceImpl implements SupplierEventListenerService {
    private final SupplierService supplierService;

    @KafkaListener(topics = "supplier-response", groupId = "supplier_response_group", concurrency = "10", containerFactory = "supplierKafkaListenerContainerFactory")
    public void handleInvoiceResponse(SupplierDTO supplierDTO) throws GenericException {
        CompletableFuture<SupplierDTO> future = supplierService.getPendingRequest(supplierDTO.getCorrelationId(), supplierDTO.getId());

        if (future != null) {
            future.complete(supplierDTO);
        } else {
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleInvoicePageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = supplierService.getPendingPageRequest(customPageDTO.getPageable().getCorrelationId());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
