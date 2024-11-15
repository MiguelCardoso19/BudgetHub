package com.paymentMicroservice.exception;

import lombok.Getter;

import java.util.List;

public class ErrorResponse {
    @Getter
    private final String message;
    @Getter
    private final int status;
    @Getter
    private final String errorCode;

    private List<String> errors;

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

}