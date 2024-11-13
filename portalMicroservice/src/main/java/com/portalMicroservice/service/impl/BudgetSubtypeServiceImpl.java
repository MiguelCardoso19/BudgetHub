package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetSubtypeService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
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
public class BudgetSubtypeServiceImpl implements BudgetSubtypeService {
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, BudgetSubtypeDTO> kafkaBudgetSubTypeTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;

    private final ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<BudgetSubtypeDTO>> pendingRequests = new ConcurrentHashMap<>();

    @Value("${kafka-timeout-duration}")
    private long TIMEOUT_DURATION;

    @Override
    public BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, TimeoutException {
        budgetSubTypeDTO.setCorrelationId(UUID.randomUUID());
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(budgetSubTypeDTO.getCorrelationId(), future);
        kafkaBudgetSubTypeTemplate.send("add-budget-subtype", budgetSubTypeDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public BudgetSubtypeDTO update(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(budgetSubTypeDTO.getId(), future);
        kafkaBudgetSubTypeTemplate.send("update-budget-subtype", budgetSubTypeDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void delete(UUID id) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        kafkaUuidTemplate.send("delete-budget-subtype", id);
        future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public BudgetSubtypeDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        kafkaUuidTemplate.send("find-budget-subtype-by-id", id);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public CustomPageDTO findAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        CustomPageableDTO customPageableDTO = PageableUtils.convertToCustomPageable(pageable);
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        kafkaPageableTemplate.send("find-all-budget-subtype", customPageableDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    public CompletableFuture<BudgetSubtypeDTO> getPendingRequest(UUID correlationId, UUID id) {
        return pendingRequests.remove(correlationId != null ? correlationId : id);
    }

    public CompletableFuture<CustomPageDTO> getPendingPageRequest(UUID correlationId) {
        return pendingPageRequests.remove(correlationId);
    }
}
