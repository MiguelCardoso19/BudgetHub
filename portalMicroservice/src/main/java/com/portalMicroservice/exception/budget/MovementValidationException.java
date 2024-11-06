package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.MOVEMENT_VALIDATION_ERROR;

@Getter
public class MovementValidationException extends Exception {
    private final HttpStatus status;
    private final String errorCode;

    public MovementValidationException(String errorMessages) {
        super(MOVEMENT_VALIDATION_ERROR.getMessage(errorMessages));
        this.status = MOVEMENT_VALIDATION_ERROR.getStatus();
        this.errorCode = MOVEMENT_VALIDATION_ERROR.getErrorCode();
    }
}