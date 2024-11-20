package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.budget.BudgetExceededException;
import com.portalMicroservice.exception.budget.BudgetSubtypeAlreadyExistsException;
import com.portalMicroservice.exception.budget.BudgetSubtypeNotFoundException;

import java.util.UUID;

public interface BudgetSubtypeEventListenerService {
    void handleBudgetSubtypeResponse(BudgetSubtypeDTO budgetSubtypeDTO);
    void handleBudgetSubtypePageResponse(CustomPageDTO customPageDTO);
    void handleDeleteSuccess(UUID id);
    void handleNotFoundExceptionResponse(BudgetSubtypeNotFoundException errorPayload);
    void handleValidationExceptionResponse(BudgetSubtypeAlreadyExistsException errorPayload);
    void handleBudgetExceededExceptionResponse(BudgetExceededException errorPayload);
}
