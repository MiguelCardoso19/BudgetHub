package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;

public interface BudgetSubtypeEventListenerService {
    void handleBudgetSubtypeResponse(BudgetSubtypeDTO budgetSubtypeDTO) throws GenericException;
    void handleBudgetSubtypePageResponse(CustomPageDTO customPageDTO) throws GenericException;
}
