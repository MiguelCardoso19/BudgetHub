package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorMessage {
    BUDGET_TYPE_NOT_FOUND("BUDGET_TYPE_NOT_FOUND", "Budget type not found with ID: %s", NOT_FOUND),
    BUDGET_SUBTYPE_NOT_FOUND("BUDGET_SUBTYPE_NOT_FOUND", "Budget subtype not found with ID: %s", NOT_FOUND);


    @Getter
    private final String errorCode;
    @Getter
    private final HttpStatus status;

    private final String message;

    ErrorMessage(String errorCode, String message, HttpStatus status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }

}