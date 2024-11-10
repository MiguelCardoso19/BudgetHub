package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.InvoiceEventListenerService;
import com.portalMicroservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class InvoiceEventListenerServiceImpl implements InvoiceEventListenerService {
    private final InvoiceService invoiceService;

    @KafkaListener(topics = "invoice-response", groupId = "invoice_response_group", concurrency = "10", containerFactory = "invoiceKafkaListenerContainerFactory")
    public void handleInvoiceResponse(InvoiceDTO invoiceDTO) throws GenericException {
        CompletableFuture<InvoiceDTO> future = invoiceService.getPendingRequest(invoiceDTO.getCorrelationId(), invoiceDTO.getId());

        if (future != null) {
            future.complete(invoiceDTO);
        } else {
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "invoice-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleInvoicePageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = invoiceService.getPendingPageRequest(customPageDTO.getPageable().getCorrelationId());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
