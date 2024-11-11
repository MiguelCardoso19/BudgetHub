package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.BudgetTypeAlreadyExistsException;
import com.portalMicroservice.exception.budget.BudgetTypeNotFoundException;

import java.util.UUID;

public interface BudgetTypeEventListenerService {
    void handleBudgetTypeResponse(BudgetTypeDTO budgetTypeDTO) throws GenericException;
    void handleBudgetTypePageResponse(CustomPageDTO customPageDTO) throws GenericException;
    void handleDeleteSuccess(UUID id) throws GenericException;
    void handleNotFoundExceptionResponse(BudgetTypeNotFoundException errorPayload);
    void handleValidationExceptionResponse(BudgetTypeAlreadyExistsException errorPayload);

}
