package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.MovementDTO;
import com.portalMicroservice.exception.budget.*;

import java.util.UUID;

public interface MovementEventListenerService {
    void handleMovementResponse(MovementDTO movementDTO);
    void handleMovementPageResponse(CustomPageDTO customPageDTO);
    void handleDeleteSuccess(UUID id);
    void handleExportReportSuccess(UUID id);
    void handleGenerateExcelExceptionResponse(GenerateExcelException errorPayload);
    void handleNotFoundExceptionResponse(MovementNotFoundException errorPayload);
    void handleValidationExceptionResponse(MovementValidationException errorPayload);
    void handleBudgetExceededExceptionExceptionResponse(BudgetExceededException errorPayload);
    void handleMovementsNotFoundForBudgetTypeExceptionResponse(MovementsNotFoundForBudgetTypeException errorPayload);
    void handleMovementsNotFoundForBudgetSubtypeExceptionResponse(MovementsNotFoundForBudgetSubtypeException errorPayload);
}
