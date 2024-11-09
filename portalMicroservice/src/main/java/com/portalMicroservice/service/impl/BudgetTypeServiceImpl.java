package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetTypeService;
import com.portalMicroservice.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetTypeServiceImpl implements BudgetTypeService {
    private final ConcurrentHashMap<UUID, CompletableFuture<BudgetTypeDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, CompletableFuture<CustomPageDTO>> pendingPageRequests = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, BudgetTypeDTO> kafkaBudgetTypeTemplate;
    private final KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;

    private static final long TIMEOUT_DURATION = 10;

    @Override
    public BudgetTypeDTO create(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);

        kafkaBudgetTypeTemplate.send("create-budget-type", budgetTypeDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while creating budget type: {}", budgetTypeDTO.getId());
            throw new GenericException();
        }
    }

    @Override
    public BudgetTypeDTO update(BudgetTypeDTO budgetTypeDTO) throws ExecutionException, InterruptedException, GenericException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"), future);

        kafkaBudgetTypeTemplate.send("update-budget-type", budgetTypeDTO);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Request timed out while updating budget type: {}", budgetTypeDTO.getId());
            throw new GenericException();
        }
    }

    @Override
    public void delete(UUID id) {
        kafkaUuidTemplate.send("delete-budget-type", id);
    }

    @Override
    public BudgetTypeDTO getById(UUID id) throws GenericException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        kafkaUuidTemplate.send("find-budget-type-by-id", id);
        log.info("Sent ID to retrieve budget type: {}", id);

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            log.error("Request timed out while retrieving budget type by ID: {}", id);
            throw new GenericException();
        }
    }

    @Override
    public CustomPageDTO findAll(Pageable pageable) throws GenericException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(pageable.getPageSize(), future);

        kafkaPageableTemplate.send("find-all-budget-type", PageableUtils.convertToCustomPageable(pageable));
        log.info("Sent pageable request for budget types");

        try {
            return future.get(TIMEOUT_DURATION, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            log.error("Request timed out while fetching budget types");
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "budget-type-response", groupId = "budget_type_response_group", concurrency = "10", containerFactory = "budgetTypeKafkaListenerContainerFactory")
    public void listen(BudgetTypeDTO budgetTypeDTO) throws GenericException {
        log.info("Received message for BudgetType: {}", budgetTypeDTO.getId());

        CompletableFuture<BudgetTypeDTO> future = pendingRequests.remove(UUID.fromString("08301b71-15ac-4569-86dc-a0350208e77d"));
        if (future != null) {
            future.complete(budgetTypeDTO);
        } else {
            log.warn("Received response for unknown or expired request ID: {}", budgetTypeDTO.getId());
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "budget-type-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void listenToPageResponse(CustomPageDTO customPageDTO) {
        log.info("Received pageable message for BudgetType");

        CompletableFuture<CustomPageDTO> future = pendingPageRequests.remove(customPageDTO.getPageable().getPageSize());
        if (future != null) {
            future.complete(customPageDTO);
        } else {
            log.warn("Received response for unknown or expired pageable request.");
        }
    }
}
