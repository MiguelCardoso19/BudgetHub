package com.authenticationMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.authenticationMicroservice.exception.ErrorMessage.INVALID_TOKEN;

@Getter
public class InvalidTokenException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public InvalidTokenException() {
        super(INVALID_TOKEN.getMessage());
        this.message = INVALID_TOKEN.getMessage();
        this.status = INVALID_TOKEN.getStatus();
        this.errorCode = INVALID_TOKEN.getErrorCode();
    }
}
