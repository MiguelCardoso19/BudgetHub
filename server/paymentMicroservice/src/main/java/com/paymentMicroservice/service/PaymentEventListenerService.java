package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.BudgetSubtypeDTO;
import com.paymentMicroservice.dto.BudgetTypeDTO;

public interface PaymentEventListenerService {
    void handleBudgetSubtypeResponse(BudgetSubtypeDTO budgetSubtypeDTO);
    void handleBudgetTypeResponse(BudgetTypeDTO budgetTypeDTO);
}