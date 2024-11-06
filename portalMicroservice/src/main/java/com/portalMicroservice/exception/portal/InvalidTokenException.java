package com.portalMicroservice.exception.portal;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.INVALID_TOKEN;

@Getter
public class InvalidTokenException extends Exception {
    private final String message;
    private final String errorCode;
    private final HttpStatus status;

    public InvalidTokenException() {
        super(INVALID_TOKEN.getMessage());
        this.message = INVALID_TOKEN.getMessage();
        this.errorCode = INVALID_TOKEN.getErrorCode();
        this.status = INVALID_TOKEN.getStatus();
    }
}