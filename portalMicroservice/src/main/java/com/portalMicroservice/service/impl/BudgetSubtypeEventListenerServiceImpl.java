package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.budget.*;
import com.portalMicroservice.service.BudgetSubtypeEventListenerService;
import com.portalMicroservice.service.BudgetSubtypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BudgetSubtypeEventListenerServiceImpl implements BudgetSubtypeEventListenerService {
    private final BudgetSubtypeService budgetSubtypeService;

    @Override
    @KafkaListener(topics = "budget-subtype-response", groupId = "budget_subtype_response_group", concurrency = "10", containerFactory = "budgetSubtypeKafkaListenerContainerFactory")
    public void handleBudgetSubtypeResponse(BudgetSubtypeDTO budgetSubtypeDTO) {
        CompletableFuture<BudgetSubtypeDTO> future = budgetSubtypeService.removePendingRequestById(budgetSubtypeDTO.getCorrelationId(), budgetSubtypeDTO.getId());
        if (future != null) {
            future.complete(budgetSubtypeDTO);
        }
    }

    @Override
    @KafkaListener(topics = "budget-subtype-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleBudgetSubtypePageResponse(CustomPageDTO customPageDTO) {
        CompletableFuture<CustomPageDTO> future = budgetSubtypeService.removePendingPageRequestById(customPageDTO.getPageable().getCorrelationId());
        if (future != null) {
            future.complete(customPageDTO);
        }
    }

    @Override
    @KafkaListener(topics = "budget-subtype-delete-success-response", groupId = "budget_subtype_delete_success_response_group", concurrency = "10")
    public void handleDeleteSuccess(UUID id) {
        CompletableFuture<BudgetSubtypeDTO> future = budgetSubtypeService.removePendingRequestById(id, null);
        if (future != null) {
            future.complete(null);
        }
    }

    @Override
    @KafkaListener(topics = "budget-subtype-not-found-exception-response", groupId = "budget_subtype_not_found_response_group", concurrency = "10", containerFactory = "budgetSubtypeNotFoundExceptionKafkaListenerContainerFactory")
    public void handleNotFoundExceptionResponse(BudgetSubtypeNotFoundException errorPayload) {
        CompletableFuture<BudgetSubtypeDTO> future = budgetSubtypeService.removePendingRequestById(UUID.fromString(errorPayload.getId()), null);
        if (future != null) {
            future.completeExceptionally(new BudgetSubtypeNotFoundException(errorPayload.getId()));
        }
    }

    @Override
    @KafkaListener(topics = "budget-subtype-already-exists-exception-response", groupId = "budget_subtype_already_exists_response_group", concurrency = "10", containerFactory = "budgetSubtypeAlreadyExistsExceptionKafkaListenerContainerFactory")
    public void handleValidationExceptionResponse(BudgetSubtypeAlreadyExistsException errorPayload) {
        CompletableFuture<BudgetSubtypeDTO> future = budgetSubtypeService.removePendingRequestById(errorPayload.getId(), null);
        if (future != null) {
            int firstQuoteIndex = errorPayload.getMessage().indexOf("'");
            int secondQuoteIndex = errorPayload.getMessage().indexOf("'", firstQuoteIndex + 1);
            String extracted = errorPayload.getMessage().substring(firstQuoteIndex + 1, secondQuoteIndex);
            future.completeExceptionally(new BudgetSubtypeAlreadyExistsException(extracted));
        }
    }

    @Override
    @KafkaListener(topics = "budget-subtype-budget-exceeded-exception-response", groupId = "budget_subtype_budget_exceeded_response_group", concurrency = "10", containerFactory = "budgetExceededExceptionKafkaListenerContainerFactory")
    public void handleBudgetExceededExceptionResponse(BudgetExceededException errorPayload) {
        CompletableFuture<BudgetSubtypeDTO> future = budgetSubtypeService.removePendingRequestById(errorPayload.getId(), null);
        if (future != null) {
            future.completeExceptionally(new BudgetExceededException(errorPayload.getMessage()));
        }
    }
}
