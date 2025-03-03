package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.budgetMicroservice.exception.ErrorMessage.MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE;

@Getter
public class MovementsNotFoundForBudgetSubtypeException extends Exception {
    private UUID correlationId;
    private UUID budgetId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public MovementsNotFoundForBudgetSubtypeException(UUID id) {
        super(MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getMessage(id));
        this.message = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getMessage(id);
        this.status = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getStatus();
        this.errorCode = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getErrorCode();
    }

    public MovementsNotFoundForBudgetSubtypeException(UUID correlationId, UUID budgetId) {
        super(MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getMessage(budgetId));
        this.correlationId = correlationId;
        this.budgetId = budgetId;
        this.message = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getMessage(budgetId);
        this.status = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getStatus();
        this.errorCode = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getErrorCode();
    }
}