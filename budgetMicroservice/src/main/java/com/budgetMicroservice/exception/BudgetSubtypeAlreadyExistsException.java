package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.budgetMicroservice.exception.ErrorMessage.BUDGET_SUBTYPE_ALREADY_EXISTS;

@Getter
public class BudgetSubtypeAlreadyExistsException extends Exception {
    private UUID id;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public BudgetSubtypeAlreadyExistsException(String name) {
        super(BUDGET_SUBTYPE_ALREADY_EXISTS.getMessage(name));
        this.message = BUDGET_SUBTYPE_ALREADY_EXISTS.getMessage(name);
        this.status = BUDGET_SUBTYPE_ALREADY_EXISTS.getStatus();
        this.errorCode = BUDGET_SUBTYPE_ALREADY_EXISTS.getErrorCode();
    }

    public BudgetSubtypeAlreadyExistsException(UUID id, String name) {
        super(BUDGET_SUBTYPE_ALREADY_EXISTS.getMessage(name));
        this.id = id;
        this.message = BUDGET_SUBTYPE_ALREADY_EXISTS.getMessage(name);
        this.status = BUDGET_SUBTYPE_ALREADY_EXISTS.getStatus();
        this.errorCode = BUDGET_SUBTYPE_ALREADY_EXISTS.getErrorCode();
    }
}