package com.authenticationMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.authenticationMicroservice.exception.ErrorMessage.INVALID_PASSWORD;

@Getter
public class InvalidPasswordException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public InvalidPasswordException() {
        super(INVALID_PASSWORD.getMessage());
        this.message = INVALID_PASSWORD.getMessage();
        this.status = INVALID_PASSWORD.getStatus();
        this.errorCode = INVALID_PASSWORD.getErrorCode();
    }
}