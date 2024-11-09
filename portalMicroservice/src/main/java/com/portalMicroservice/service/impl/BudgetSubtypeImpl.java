package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetSubtypeService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final ConcurrentHashMap<Integer, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, CompletableFuture<BudgetSubtypeDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, BudgetSubtypeDTO> kafkaBudgetSubTypeTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;

    private static final long TIMEOUT_DURATION = 10;

    @Override
    public BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);
        kafkaBudgetSubTypeTemplate.send("add-budget-subtype", budgetSubTypeDTO);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public BudgetSubtypeDTO update(BudgetSubtypeDTO budgetSubTypeDTO) throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);
        kafkaBudgetSubTypeTemplate.send("update-budget-subtype", budgetSubTypeDTO);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public void delete(UUID id) {
        kafkaUuidTemplate.send("delete-budget-subtype", id);
    }

    @Override
    public BudgetSubtypeDTO getById(UUID id) throws GenericException, ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);
        kafkaUuidTemplate.send("find-budget-subtype-by-id", id);
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public CustomPageDTO findAll(Pageable pageable) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(pageable.getPageSize(), future);
        kafkaPageableTemplate.send("find-all-budget-subtype", PageableUtils.convertToCustomPageable(pageable));
        return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
    }

    @KafkaListener(topics = "budget-subtype-response", groupId = "budget_subtype_response_group", concurrency = "10", containerFactory = "budgetSubtypeKafkaListenerContainerFactory")
    public void listen(BudgetSubtypeDTO budgetSubTypeDTO) throws GenericException {
        CompletableFuture<BudgetSubtypeDTO> future = pendingRequests.remove(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"));

        if (future != null) {
            future.complete(budgetSubTypeDTO);
        } else {
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "budget-subtype-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void listenToPageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = pendingPageRequests.remove(customPageDTO.getPageable().getPageSize());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
