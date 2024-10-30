package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.budgetMicroservice.exception.ErrorMessage.BUDGET_TYPE_ALREADY_EXISTS;

@Getter
public class BudgetTypeAlreadyExistsException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public BudgetTypeAlreadyExistsException(String name) {
        super(BUDGET_TYPE_ALREADY_EXISTS.getMessage(name));
        this.message = BUDGET_TYPE_ALREADY_EXISTS.getMessage(name);
        this.status = BUDGET_TYPE_ALREADY_EXISTS.getStatus();
        this.errorCode = BUDGET_TYPE_ALREADY_EXISTS.getErrorCode();
    }
}
