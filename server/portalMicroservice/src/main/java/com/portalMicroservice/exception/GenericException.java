package com.portalMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.GENERIC_MESSAGE;

@Getter
public class GenericException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public GenericException() {
        super(GENERIC_MESSAGE.getMessage());
        this.message = GENERIC_MESSAGE.getMessage();
        this.status = GENERIC_MESSAGE.getStatus();
        this.errorCode = GENERIC_MESSAGE.getErrorCode();
    }
}