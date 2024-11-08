package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.AttachFileRequestDTO;
import com.portalMicroservice.dto.budget.InvoiceDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;
    private final KafkaTemplate<String, AttachFileRequestDTO> kafkaAttachFileRequestTemplate;
    private final ConcurrentHashMap<UUID, CompletableFuture<InvoiceDTO>> pendingRequests = new ConcurrentHashMap<>();

    private static final long TIMEOUT_DURATION = 10;

    @Override
    public InvoiceDTO create(InvoiceDTO invoiceDTO) throws GenericException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);

        kafkaInvoiceTemplate.send("create-invoice", invoiceDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new GenericException();
        }
    }

    @Override
    public InvoiceDTO update(InvoiceDTO invoiceDTO) throws GenericException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);

        kafkaInvoiceTemplate.send("update-invoice", invoiceDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new GenericException();
        }
    }

    @Override
    public void delete(UUID id) throws GenericException {
        kafkaUuidTemplate.send("delete-invoice", id);
    }

    @Override
    public InvoiceDTO getById(UUID id) throws GenericException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);

        kafkaUuidTemplate.send("get-by-id-invoice", id);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            throw new GenericException();
        }
    }

    @Override
    public void attachBase64FileToInvoice(AttachFileRequestDTO attachFileRequestDTO) {
        kafkaAttachFileRequestTemplate.send("attach-base64-file-to-invoice", attachFileRequestDTO);
    }


    @KafkaListener(topics = "invoice-response", groupId = "invoice_response_group", concurrency = "10", containerFactory = "invoiceKafkaListenerContainerFactory")
    public void listen(InvoiceDTO invoiceDTO) throws GenericException {
        CompletableFuture<InvoiceDTO> future = pendingRequests.remove(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"));

        if (future != null) {
            future.complete(invoiceDTO);
        } else {
            throw new GenericException();

        }
    }
}
