package com.portalMicroservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final ConcurrentHashMap<UUID, CompletableFuture<SupplierDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final long TIMEOUT_DURATION = 30;

    @Override
    public SupplierDTO create(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();

        kafkaSupplierTemplate.send("create-supplier", supplierDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while creating supplier: {}", supplierDTO.getCompanyName());
            throw new GenericException();
        }
    }

    @Override
    public SupplierDTO update(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();

        kafkaSupplierTemplate.send("update-supplier", supplierDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while updating supplier: {}", supplierDTO.getCompanyName());
            throw new GenericException();
        }
    }

    @Override
    public void delete(UUID id) {
        kafkaUuidTemplate.send("delete-supplier", id);
    }

    @Override
    public SupplierDTO getById(UUID id) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        kafkaUuidTemplate.send("find-by-id-supplier", id);
        log.info("Sent ID to supplier: {}", id);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while retrieving supplier by ID: {}", id);
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "supplier-response", groupId = "response_group", concurrency = "10")
    public void listen(String message) throws JsonProcessingException, GenericException {
        SupplierDTO supplierDTO = objectMapper.readValue(message, SupplierDTO.class);
        log.info("Received message: {}", supplierDTO.getCompanyName());

        CompletableFuture<SupplierDTO> future = pendingRequests.remove(supplierDTO.getId());
        if (future != null) {
            future.complete(supplierDTO);
        } else {
            log.warn("Received response for unknown or expired request ID: {}", supplierDTO.getId());
            throw new GenericException();

        }
    }
}
