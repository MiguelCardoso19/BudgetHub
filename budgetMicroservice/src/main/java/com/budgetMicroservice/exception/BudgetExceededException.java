package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.budgetMicroservice.exception.ErrorMessage.BUDGET_EXCEEDED;

@Getter
public class BudgetExceededException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public BudgetExceededException(Double totalValue, Double totalSpent) {
        super(BUDGET_EXCEEDED.getMessage(totalValue, totalSpent));
        this.message = BUDGET_EXCEEDED.getMessage(totalValue, totalSpent);
        this.status = BUDGET_EXCEEDED.getStatus();
        this.errorCode = BUDGET_EXCEEDED.getErrorCode();
    }
}
