package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.budget.BudgetTypeAlreadyExistsException;
import com.portalMicroservice.exception.budget.BudgetTypeNotFoundException;

import java.util.UUID;

public interface BudgetTypeEventListenerService {
    void handleBudgetTypeResponse(BudgetTypeDTO budgetTypeDTO);
    void handleBudgetTypePageResponse(CustomPageDTO customPageDTO);
    void handleDeleteSuccess(UUID id);
    void handleNotFoundExceptionResponse(BudgetTypeNotFoundException errorPayload);
    void handleValidationExceptionResponse(BudgetTypeAlreadyExistsException errorPayload);

}
