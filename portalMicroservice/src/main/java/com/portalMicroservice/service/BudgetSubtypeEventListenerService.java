package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.BudgetExceededException;
import com.portalMicroservice.exception.budget.BudgetSubtypeAlreadyExistsException;
import com.portalMicroservice.exception.budget.BudgetSubtypeNotFoundException;

import java.util.UUID;

public interface BudgetSubtypeEventListenerService {
    void handleBudgetSubtypeResponse(BudgetSubtypeDTO budgetSubtypeDTO) throws GenericException;
    void handleBudgetSubtypePageResponse(CustomPageDTO customPageDTO) throws GenericException;
    void handleDeleteSuccess(UUID id) throws GenericException;
    void handleNotFoundExceptionResponse(BudgetSubtypeNotFoundException errorPayload);
    void handleValidationExceptionResponse(BudgetSubtypeAlreadyExistsException errorPayload);
    void handleBudgetExceededExceptionResponse(BudgetExceededException errorPayload);
}
