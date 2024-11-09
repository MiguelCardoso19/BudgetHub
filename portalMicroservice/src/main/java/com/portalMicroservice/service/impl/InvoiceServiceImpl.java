package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.*;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.service.InvoiceService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;
    private final KafkaTemplate<String, AttachFileRequestDTO> kafkaAttachFileRequestTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;
    private final ConcurrentHashMap<UUID, CompletableFuture<InvoiceDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();

    private static final long TIMEOUT_DURATION = 10;

    @Override
    public InvoiceDTO create(InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);
        kafkaInvoiceTemplate.send("create-invoice", invoiceDTO);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public InvoiceDTO update(InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);
        kafkaInvoiceTemplate.send("update-invoice", invoiceDTO);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public void delete(UUID id) throws GenericException {
        kafkaUuidTemplate.send("delete-invoice", id);
    }

    @Override
    public InvoiceDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);
        kafkaUuidTemplate.send("get-by-id-invoice", id);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }
    @Override
    public CustomPageDTO getAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(pageable.getPageSize(), future);
        kafkaPageableTemplate.send("get-all-invoices", PageableUtils.convertToCustomPageable(pageable));
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public void attachBase64FileToInvoice(UUID invoiceId, MultipartFile file) throws FailedToUploadFileException {
        try {
            byte[] fileBytes = file.getBytes();
            String base64File = Base64.getEncoder().encodeToString(fileBytes);
            kafkaAttachFileRequestTemplate.send("attach-base64-file-to-invoice", new AttachFileRequestDTO(invoiceId, base64File));
        } catch (IOException e) {
            throw new FailedToUploadFileException(invoiceId);
        }
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

    @KafkaListener(topics = "invoice-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void listenToPageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = pendingPageRequests.remove(customPageDTO.getPageable().getPageSize());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
