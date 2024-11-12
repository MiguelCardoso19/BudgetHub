package com.authenticationMicroservice.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorMessage {
    INVALID_PASSWORD("INVALID_PASSWORD", "This password is not valid", UNAUTHORIZED),
    INVALID_TOKEN("INVALID_TOKEN", "This token is not valid", UNAUTHORIZED),
    USERNAME_ALREADY_EXISTS("USERNAME_ALREADY_EXISTS", "This username already exists: %s", CONFLICT),
    USER_CREDENTIALS_VALIDATION_ERROR("USER_CREDENTIALS_VALIDATION_ERROR", "User operation failed due to the following error/s: %s", CONFLICT),
    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "Username not found: %s", NOT_FOUND),
    EMAIL_NOT_FOUND("EMAIL_NOT_FOUND", "Email not found: %s", NOT_FOUND),
    NIF_NOT_FOUND("NIF_NOT_FOUND", "NIF not found: %s", NOT_FOUND),
    USER_ID_NOT_FOUND("USER_ID_NOT_FOUND", "User ID not found: %s", NOT_FOUND);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;

    ErrorMessage(String errorCode, String message, HttpStatus status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}