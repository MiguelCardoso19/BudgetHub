package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.*;

import java.util.UUID;

public interface MovementEventListenerService {
    void handleMovementResponse(MovementDTO movementDTO) throws GenericException;
    void handleMovementPageResponse(CustomPageDTO customPageDTO) throws GenericException;
    void handleDeleteSuccess(UUID id) throws GenericException;
    void handleExportReportSuccess(UUID id) throws GenericException;
    void handleGenerateExcelExceptionResponse(GenerateExcelException errorPayload);
    void handleNotFoundExceptionResponse(MovementNotFoundException errorPayload);
    void handleValidationExceptionResponse(MovementValidationException errorPayload);
    void handleBudgetExceededExceptionExceptionResponse(BudgetExceededException errorPayload);
    void handleMovementsNotFoundForBudgetTypeExceptionResponse(MovementsNotFoundForBudgetTypeException errorPayload);
    void handleMovementsNotFoundForBudgetSubtypeExceptionResponse(MovementsNotFoundForBudgetSubtypeException errorPayload);
}
