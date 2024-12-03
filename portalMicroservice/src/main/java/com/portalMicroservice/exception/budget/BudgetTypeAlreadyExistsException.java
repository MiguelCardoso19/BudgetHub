package com.portalMicroservice.exception.budget;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.portalMicroservice.exception.ErrorMessage.BUDGET_TYPE_ALREADY_EXISTS;

@Data
public class BudgetTypeAlreadyExistsException extends Exception {
    private UUID id;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public BudgetTypeAlreadyExistsException(UUID id, String name) {
        super(BUDGET_TYPE_ALREADY_EXISTS.getMessage(name));
        this.id = id;
        this.message = BUDGET_TYPE_ALREADY_EXISTS.getMessage(name);
        this.status = BUDGET_TYPE_ALREADY_EXISTS.getStatus();
        this.errorCode = BUDGET_TYPE_ALREADY_EXISTS.getErrorCode();
    }

    public BudgetTypeAlreadyExistsException(String name) {
        super(BUDGET_TYPE_ALREADY_EXISTS.getMessage(name));
        this.message = BUDGET_TYPE_ALREADY_EXISTS.getMessage(name);
        this.status = BUDGET_TYPE_ALREADY_EXISTS.getStatus();
        this.errorCode = BUDGET_TYPE_ALREADY_EXISTS.getErrorCode();
    }
}
