package com.notificationMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorMessage {
    FAILED_TO_SEND_EMAIL("FAILED_TO_SEND_EMAIL", "Failed to send email to: %s", CONFLICT);

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