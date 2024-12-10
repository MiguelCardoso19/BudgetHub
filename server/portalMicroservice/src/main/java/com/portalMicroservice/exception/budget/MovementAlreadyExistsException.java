package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.MOVEMENT_ALREADY_EXISTS;

@Getter
public class MovementAlreadyExistsException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public MovementAlreadyExistsException(String documentNumber) {
        super(MOVEMENT_ALREADY_EXISTS.getMessage(documentNumber));
        this.message = MOVEMENT_ALREADY_EXISTS.getMessage(documentNumber);
        this.status = MOVEMENT_ALREADY_EXISTS.getStatus();
        this.errorCode = MOVEMENT_ALREADY_EXISTS.getErrorCode();
    }
}