package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.BUDGET_EXCEEDED;

@Getter
public class BudgetExceededException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public BudgetExceededException(String message) {
        super(BUDGET_EXCEEDED.getMessage(message));
        this.message = BUDGET_EXCEEDED.getMessage(message);
        this.status = BUDGET_EXCEEDED.getStatus();
        this.errorCode = BUDGET_EXCEEDED.getErrorCode();
    }
}
