package com.portalMicroservice.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum ErrorMessage {
    INVALID_TOKEN("INVALID_TOKEN", "Invalid token or username mismatch", HttpStatus.UNAUTHORIZED),
    USER_ID_NOT_FOUND("USER_ID_NOT_FOUND", "User not found with ID: %s", HttpStatus.NOT_FOUND),
    USER_NAME_NOT_FOUND("USER_NAME_NOT_FOUND", "User not found with name: %s", HttpStatus.NOT_FOUND),
    INVALID_AUTHORIZATION_HEADER("INVALID_AUTHORIZATION_HEADER", "Authorization header missing or invalid", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("INVALID_PASSWORD", "This password is not valid", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("ROLE_ACCESS_DENIED", "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", "This refresh token is not valid", HttpStatus.UNAUTHORIZED),
    GENERIC_MESSAGE("GENERIC_MESSAGE", "An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_NOT_FOUND("EMAIL_NOT_FOUND","Email not found: %s", NOT_FOUND),
    USER_CREDENTIALS_VALIDATION_ERROR("USER_CREDENTIALS_VALIDATION_ERROR", "User operation failed due to the following error/s: %s", HttpStatus.CONFLICT);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;

    ErrorMessage(String code, String message, HttpStatus status) {
        this.errorCode = code;
        this.message = message;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
