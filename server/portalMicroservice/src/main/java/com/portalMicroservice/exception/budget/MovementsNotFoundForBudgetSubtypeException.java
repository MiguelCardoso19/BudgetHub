package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE;

@Getter
public class MovementsNotFoundForBudgetSubtypeException extends Exception {
    private String correlationId;
    private String budgetId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public MovementsNotFoundForBudgetSubtypeException(String id) {
        super(MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getMessage(id));
        this.budgetId = id;
        this.message = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getMessage(id);
        this.status = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getStatus();
        this.errorCode = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getErrorCode();
    }

    public MovementsNotFoundForBudgetSubtypeException(String correlationId, String budgetId) {
        super(MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getMessage(budgetId));
        this.correlationId = correlationId;
        this.budgetId = budgetId;
        this.message = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getMessage(budgetId);
        this.status = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getStatus();
        this.errorCode = MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE.getErrorCode();
    }
}