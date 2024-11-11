package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.BUDGET_TYPE_NOT_FOUND;

@Getter
public class BudgetTypeNotFoundException extends Exception {
    private final String id;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public BudgetTypeNotFoundException(String id) {
        super(BUDGET_TYPE_NOT_FOUND.getMessage(id));
        this.id = id;
        this.message = BUDGET_TYPE_NOT_FOUND.getMessage(id);
        this.status = BUDGET_TYPE_NOT_FOUND.getStatus();
        this.errorCode = BUDGET_TYPE_NOT_FOUND.getErrorCode();
    }
}