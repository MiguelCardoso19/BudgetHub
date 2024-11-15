package com.paymentMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorMessage {
    FAILED_TO_CANCEL_PAYMENT("FAILED_TO_CANCEL_PAYMENT", "Failed to cancel the payment", CONFLICT),
    FAILED_TO_CONFIRM_PAYMENT("FAILED_TO_CONFIRM_PAYMENT", "Failed to confirm the payment", CONFLICT);

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