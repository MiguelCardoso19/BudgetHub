package com.paymentMicroservice.service.impl;

import com.paymentMicroservice.dto.BudgetSubtypeDTO;
import com.paymentMicroservice.dto.BudgetTypeDTO;
import com.paymentMicroservice.service.PaymentEventListenerService;
import com.paymentMicroservice.validator.PaymentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentEventListenerServiceImpl implements PaymentEventListenerService {
    private final PaymentValidator paymentValidator;

    @Override
    @KafkaListener(topics = "budget-subtype-payment-response", groupId = "budget_subtype_payment_response_group", concurrency = "10", containerFactory = "budgetSubtypeKafkaListenerContainerFactory")
    public void handleBudgetSubtypeResponse(BudgetSubtypeDTO budgetSubtypeDTO) {
        CompletableFuture<BudgetSubtypeDTO> future = paymentValidator.removePendingSubtypeRequestById(budgetSubtypeDTO.getId());
        if (future != null) {
            future.complete(budgetSubtypeDTO);
        }
    }

    @Override
    @KafkaListener(topics = "budget-type-payment-response", groupId = "budget_type_payment_response_group", concurrency = "10", containerFactory = "budgetTypeKafkaListenerContainerFactory")
    public void handleBudgetTypeResponse(BudgetTypeDTO budgetTypeDTO) {
        CompletableFuture<BudgetTypeDTO> future = paymentValidator.removePendingTypeRequestById(budgetTypeDTO.getId());
        if (future != null) {
            future.complete(budgetTypeDTO);
        }
    }
}