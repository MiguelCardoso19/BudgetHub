package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.BudgetTypeAlreadyExistsException;
import com.portalMicroservice.exception.budget.BudgetTypeNotFoundException;
import com.portalMicroservice.service.BudgetTypeEventListenerService;
import com.portalMicroservice.service.BudgetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BudgetTypeEventListenerServiceImpl implements BudgetTypeEventListenerService {
    private final BudgetTypeService budgetTypeService;

    @Override
    @KafkaListener(topics = "budget-type-response", groupId = "budget_type_response_group", concurrency = "10", containerFactory = "budgetTypeKafkaListenerContainerFactory")
    public void handleBudgetTypeResponse(BudgetTypeDTO budgetTypeDTO) throws GenericException {
        CompletableFuture<BudgetTypeDTO> future = budgetTypeService.removePendingRequestById(budgetTypeDTO.getCorrelationId(), budgetTypeDTO.getId());

        if (future != null) {
            future.complete(budgetTypeDTO);
        } else {
            throw new GenericException();
        }
    }

    @Override
    @KafkaListener(topics = "budget-type-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleBudgetTypePageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = budgetTypeService.removePendingPageRequestById(customPageDTO.getPageable().getCorrelationId());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }

    @Override
    @KafkaListener(topics = "budget-type-delete-success-response", groupId = "budget_type_delete_success_response_group", concurrency = "10")
    public void handleDeleteSuccess(UUID id) throws GenericException {
        CompletableFuture<BudgetTypeDTO> future = budgetTypeService.removePendingRequestById(id, null);

        if (future != null) {
            future.complete(null);
        } else {
            throw new GenericException();
        }
    }

    @Override
    @KafkaListener(topics = "budget-type-not-found-exception-response", groupId = "budget_type_not_found_response_group", concurrency = "10", containerFactory = "budgetTypeNotFoundExceptionKafkaListenerContainerFactory")
    public void handleNotFoundExceptionResponse(BudgetTypeNotFoundException errorPayload) {
        CompletableFuture<BudgetTypeDTO> future = budgetTypeService.removePendingRequestById(UUID.fromString(errorPayload.getId()), null);

        if (future != null) {
            future.completeExceptionally(new BudgetTypeNotFoundException(errorPayload.getId()));
        }
    }

    @Override
    @KafkaListener(topics = "budget-type-already-exists-exception-response", groupId = "budget_type_already_exists_response_group", concurrency = "10", containerFactory = "budgetTypeAlreadyExistsExceptionKafkaListenerContainerFactory")
    public void handleValidationExceptionResponse(BudgetTypeAlreadyExistsException errorPayload) {
        CompletableFuture<BudgetTypeDTO> future = budgetTypeService.removePendingRequestById(errorPayload.getId(), null);

        if (future != null) {
            int firstQuoteIndex = errorPayload.getMessage().indexOf("'");
            int secondQuoteIndex = errorPayload.getMessage().indexOf("'", firstQuoteIndex + 1);
            String extracted = errorPayload.getMessage().substring(firstQuoteIndex + 1, secondQuoteIndex);
            future.completeExceptionally(new BudgetTypeAlreadyExistsException(extracted));
        }
    }
}
