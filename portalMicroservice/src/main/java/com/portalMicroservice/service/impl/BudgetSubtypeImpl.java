package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetSubtypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
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
public class BudgetSubtypeImpl implements BudgetSubtypeService {

    private final ConcurrentHashMap<UUID, CompletableFuture<BudgetSubtypeDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, BudgetSubtypeDTO> kafkaBudgetSubTypeTemplate;

    private static final long TIMEOUT_DURATION = 10;

    @Override
    public BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);

        kafkaBudgetSubTypeTemplate.send("add-budget-subtype", budgetSubTypeDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while creating budget sub-type: {}", budgetSubTypeDTO.getId());
            throw new GenericException();
        }
    }

    @Override
    public BudgetSubtypeDTO update(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);

        kafkaBudgetSubTypeTemplate.send("update-budget-subtype", budgetSubTypeDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while updating budget subtype: {}", budgetSubTypeDTO.getId());
            throw new GenericException();
        }
    }

    @Override
    public void delete(UUID id) {
        kafkaUuidTemplate.send("delete-budget-subtype", id);
    }

    @Override
    public BudgetSubtypeDTO getById(UUID id) throws GenericException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);

        kafkaUuidTemplate.send("find-budget-subtype-by-id", id);
        log.info("Sent ID to retrieve budget sub-type: {}", id);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            log.error("Request timed out while retrieving budget sub-type by ID: {}", id);
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "budget-subtype-response", groupId = "budget_subtype_response_group", concurrency = "10", containerFactory = "budgetSubtypeKafkaListenerContainerFactory")
    public void listen(BudgetSubtypeDTO budgetSubTypeDTO) throws GenericException {
        log.info("Received message for BudgetSubType: {}", budgetSubTypeDTO.getId());

        CompletableFuture<BudgetSubtypeDTO> future = pendingRequests.remove(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"));
        if (future != null) {
            future.complete(budgetSubTypeDTO);
        } else {
            log.warn("Received response for unknown or expired request ID: {}", budgetSubTypeDTO.getId());
            throw new GenericException();
        }
    }
}
