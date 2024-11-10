package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetTypeEventListenerService;
import com.portalMicroservice.service.BudgetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BudgetTypeEventListenerServiceImpl implements BudgetTypeEventListenerService {
    private final BudgetTypeService budgetTypeService;

    @KafkaListener(topics = "budget-type-response", groupId = "budget_type_response_group", concurrency = "10", containerFactory = "budgetTypeKafkaListenerContainerFactory")
    public void handleBudgetTypeResponse(BudgetTypeDTO budgetTypeDTO) throws GenericException {
        CompletableFuture<BudgetTypeDTO> future = budgetTypeService.getPendingRequest(budgetTypeDTO.getCorrelationId(), budgetTypeDTO.getId());

        if (future != null) {
            future.complete(budgetTypeDTO);
        } else {
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "budget-type-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleBudgetTypePageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = budgetTypeService.getPendingPageRequest(customPageDTO.getPageable().getCorrelationId());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
