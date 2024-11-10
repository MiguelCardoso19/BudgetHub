package com.portalMicroservice.service.impl;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.BudgetSubtypeEventListenerService;
import com.portalMicroservice.service.BudgetSubtypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BudgetSubtypeEventListenerServiceImpl implements BudgetSubtypeEventListenerService {
    private final BudgetSubtypeService budgetSubtype;

    @KafkaListener(topics = "budget-subtype-response", groupId = "budget_subtype_response_group", concurrency = "10", containerFactory = "budgetSubtypeKafkaListenerContainerFactory")
    public void handleBudgetSubtypeResponse(BudgetSubtypeDTO budgetSubtypeDTO) throws GenericException {
        CompletableFuture<BudgetSubtypeDTO> future = budgetSubtype.getPendingRequest(budgetSubtypeDTO.getCorrelationId(), budgetSubtypeDTO.getId());

        if (future != null) {
            future.complete(budgetSubtypeDTO);
        } else {
            throw new GenericException();
        }
    }

    @KafkaListener(topics = "budget-subtype-page-response", groupId = "pageable_response_group", concurrency = "10", containerFactory = "customPageKafkaListenerContainerFactory")
    public void handleBudgetSubtypePageResponse(CustomPageDTO customPageDTO) throws GenericException {
        CompletableFuture<CustomPageDTO> future = budgetSubtype.getPendingPageRequest(customPageDTO.getPageable().getCorrelationId());

        if (future != null) {
            future.complete(customPageDTO);
        } else {
            throw new GenericException();
        }
    }
}
