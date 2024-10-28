package com.portalMicroservice.exception.authentication;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.INVALID_PASSWORD;

@Getter
public class InvalidPasswordException extends Exception {
    private final String message;
    private final String errorCode;
    private final HttpStatus status;

    public InvalidPasswordException() {
        super(INVALID_PASSWORD.getMessage());
        this.message = INVALID_PASSWORD.getMessage();
        this.errorCode = INVALID_PASSWORD.getErrorCode();
        this.status = INVALID_PASSWORD.getStatus();
    }
}