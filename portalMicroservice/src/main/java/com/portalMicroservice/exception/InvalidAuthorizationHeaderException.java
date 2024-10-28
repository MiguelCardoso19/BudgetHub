package com.portalMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.INVALID_AUTHORIZATION_HEADER;

@Getter
public class InvalidAuthorizationHeaderException extends Exception {
    private final String message;
    private final String errorCode;
    private final HttpStatus status;

    public InvalidAuthorizationHeaderException() {
        super(INVALID_AUTHORIZATION_HEADER.getMessage());
        this.message = INVALID_AUTHORIZATION_HEADER.getMessage();
        this.errorCode = INVALID_AUTHORIZATION_HEADER.getErrorCode();
        this.status = INVALID_AUTHORIZATION_HEADER.getStatus();
    }
}