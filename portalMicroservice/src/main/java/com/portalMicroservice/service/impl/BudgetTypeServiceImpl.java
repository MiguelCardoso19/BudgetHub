package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetTypeService;
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
public class BudgetTypeServiceImpl implements BudgetTypeService {
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, BudgetTypeDTO> kafkaBudgetTypeTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;

    private final ConcurrentHashMap<UUID, CompletableFuture<BudgetTypeDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();

    @Value("${kafka-timeout-duration}")
    private long TIMEOUT_DURATION;

    @Override
    public BudgetTypeDTO create(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        budgetTypeDTO.setCorrelationId(UUID.randomUUID());
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(budgetTypeDTO.getCorrelationId(), future);
        kafkaBudgetTypeTemplate.send("create-budget-type", budgetTypeDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public BudgetTypeDTO update(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(budgetTypeDTO.getCorrelationId(), future);
        kafkaBudgetTypeTemplate.send("update-budget-type", budgetTypeDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public void delete(UUID id) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        kafkaUuidTemplate.send("delete-budget-type", id);
        future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public BudgetTypeDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        kafkaUuidTemplate.send("find-budget-type-by-id", id);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    @Override
    public CustomPageDTO findAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        CustomPageableDTO customPageableDTO = PageableUtils.convertToCustomPageable(pageable);
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        kafkaPageableTemplate.send("find-all-budget-type", customPageableDTO);
        return future.get(TIMEOUT_DURATION, SECONDS);
    }

    public CompletableFuture<BudgetTypeDTO> getPendingRequest(UUID correlationId, UUID id) {
        return pendingRequests.remove(correlationId != null ? correlationId : id);
    }

    public CompletableFuture<CustomPageDTO> getPendingPageRequest(UUID correlationId) {
        return pendingPageRequests.remove(correlationId);
    }
}
