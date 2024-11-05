package com.notificationMicroservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private final String message;

    private final int status;

    private final String errorCode;

    public ErrorResponse(String message, int status, String errorCode) {
        this.message = message;
        this.status = status;
        this.errorCode = errorCode;
    }
}