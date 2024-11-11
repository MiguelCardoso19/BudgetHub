package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE;

@Getter
public class MovementsNotFoundForBudgetTypeException extends Exception {
    private String correlationId;
    private String budgetId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public MovementsNotFoundForBudgetTypeException(String id) {
        super(MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getMessage(id));
        this.budgetId = id;
        this.message = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getMessage(id);
        this.status = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getStatus();
        this.errorCode = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getErrorCode();
    }

    public MovementsNotFoundForBudgetTypeException(String correlationId, String budgetId) {
        super(MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getMessage(budgetId));
        this.correlationId = correlationId;
        this.budgetId = budgetId;
        this.message = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getMessage(budgetId);
        this.status = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getStatus();
        this.errorCode = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getErrorCode();
    }
}
