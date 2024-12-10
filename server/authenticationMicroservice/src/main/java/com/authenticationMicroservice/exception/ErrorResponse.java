package com.authenticationMicroservice.exception;

import java.util.List;

public class ErrorResponse {
    private final String message;
    private final int status;
    private List<String> errors;
    private final String errorCode;

    public ErrorResponse(String message, List<String> errors, int status, String errorCode) {
        this.errors = errors;
        this.message = message;
        this.status = status;
        this.errorCode = errorCode;
    }

    public ErrorResponse(String message, int status, String errorCode) {
        this.message = message;
        this.status = status;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}