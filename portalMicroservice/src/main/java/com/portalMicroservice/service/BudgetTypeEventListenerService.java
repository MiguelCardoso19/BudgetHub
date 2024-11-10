package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;

public interface BudgetTypeEventListenerService {
    void handleBudgetTypeResponse(BudgetTypeDTO budgetTypeDTO) throws GenericException;
    void handleBudgetTypePageResponse(CustomPageDTO customPageDTO) throws GenericException;
}
