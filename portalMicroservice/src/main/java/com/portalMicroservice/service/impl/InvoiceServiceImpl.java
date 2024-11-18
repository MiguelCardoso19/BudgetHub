package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.*;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.service.InvoiceService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;
    private final KafkaTemplate<String, AttachFileRequestDTO> kafkaAttachFileRequestTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;

    private final ConcurrentHashMap<UUID, CompletableFuture<InvoiceDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();

    @Value("${kafka-timeout-duration}")
    private long TIMEOUT_DURATION;

    @Override
    public InvoiceDTO create(InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        invoiceDTO.setCorrelationId(UUID.randomUUID());
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(invoiceDTO.getCorrelationId(), future);
        kafkaInvoiceTemplate.send("create-invoice", invoiceDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public InvoiceDTO update(InvoiceDTO invoiceDTO) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(invoiceDTO.getId(), future);
        kafkaInvoiceTemplate.send("update-invoice", invoiceDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void delete(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        kafkaUuidTemplate.send("delete-invoice", id);
        future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public InvoiceDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        kafkaUuidTemplate.send("get-by-id-invoice", id);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public CustomPageDTO getAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        CustomPageableDTO customPageableDTO = PageableUtils.convertToCustomPageable(pageable);
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        kafkaPageableTemplate.send("get-all-invoices", customPageableDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void attachBase64FileToInvoice(UUID invoiceId, MultipartFile file) throws FailedToUploadFileException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(invoiceId, future);
        try {
            byte[] fileBytes = file.getBytes();
            String base64File = Base64.getEncoder().encodeToString(fileBytes);
            kafkaAttachFileRequestTemplate.send("attach-base64-file-to-invoice", new AttachFileRequestDTO(invoiceId, base64File));
        } catch (IOException e) {
            throw new FailedToUploadFileException(invoiceId);
        }
        future.get(TIMEOUT_DURATION, SECONDS);
    }

    public CompletableFuture<InvoiceDTO> removePendingRequestById(UUID correlationId, UUID id) {
        return pendingRequests.remove(correlationId != null ? correlationId : id);
    }

    public CompletableFuture<CustomPageDTO> removePendingPageRequestById(UUID correlationId) {
        return pendingPageRequests.remove(correlationId);
    }
}