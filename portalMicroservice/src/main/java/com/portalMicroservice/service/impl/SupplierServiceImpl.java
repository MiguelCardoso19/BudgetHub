package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.SupplierService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;

    private final ConcurrentHashMap<UUID, CompletableFuture<SupplierDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();

    @Value("${kafka-timeout-duration}")
    private long TIMEOUT_DURATION;

    @Override
    public SupplierDTO create(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        supplierDTO.setCorrelationId(UUID.randomUUID());
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(supplierDTO.getCorrelationId(), future);
        kafkaSupplierTemplate.send("create-supplier", supplierDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public SupplierDTO update(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(supplierDTO.getId(), future);
        kafkaSupplierTemplate.send("update-supplier", supplierDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void delete(UUID id) {
        kafkaUuidTemplate.send("delete-supplier", id);
    }

    @Override
    public SupplierDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        kafkaUuidTemplate.send("find-by-id-supplier", id);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public CustomPageDTO getAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        CustomPageableDTO customPageableDTO = PageableUtils.convertToCustomPageable(pageable);
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        kafkaPageableTemplate.send("find-all-suppliers", customPageableDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    public CompletableFuture<SupplierDTO> getPendingRequest(UUID correlationId, UUID id) {
        return pendingRequests.remove(correlationId != null ? correlationId : id);
    }

    public CompletableFuture<CustomPageDTO> getPendingPageRequest(UUID correlationId) {
        return pendingPageRequests.remove(correlationId);
    }
}
