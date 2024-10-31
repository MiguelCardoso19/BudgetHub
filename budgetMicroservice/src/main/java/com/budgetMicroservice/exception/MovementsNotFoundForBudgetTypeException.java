package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.budgetMicroservice.exception.ErrorMessage.MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE;

@Getter
public class MovementsNotFoundForBudgetTypeException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public MovementsNotFoundForBudgetTypeException(UUID id) {
        super(MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getMessage(id));
        this.message = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getMessage(id);
        this.status = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getStatus();
        this.errorCode = MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE.getErrorCode();
    }
}
