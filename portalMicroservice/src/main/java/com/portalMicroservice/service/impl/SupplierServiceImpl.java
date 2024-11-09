package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.SupplierService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final ConcurrentHashMap<Integer, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;

    private static final long TIMEOUT_DURATION = 10;

    @Override
    public SupplierDTO create(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);
        kafkaSupplierTemplate.send("create-supplier", supplierDTO);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public SupplierDTO update(SupplierDTO supplierDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);
        kafkaSupplierTemplate.send("update-supplier", supplierDTO);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
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
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public CustomPageDTO getAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(pageable.getPageSize(), future);
        kafkaPageableTemplate.send("find-all-suppliers", PageableUtils.convertToCustomPageable(pageable));
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @KafkaListener(topics = "supplier-response", groupId = "supplier_response_group", concurrency = "10", containerFactory = "supplierKafkaListenerContainerFactory")
    public void listen(SupplierDTO supplierDTO) throws GenericException {
        CompletableFuture<SupplierDTO> future = pendingRequests.remove(supplierDTO.getId());

        if (future != null) {
            future.complete(supplierDTO);
        } else {
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void listen(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = pendingPageRequests.remove(customPageDTO.getPageSize());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
