package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.budgetMicroservice.exception.ErrorMessage.MOVEMENT_VALIDATION_ERROR;

@Getter
public class MovementValidationException extends Exception {
    private final List<String> errors;
    private final HttpStatus status;
    private final String errorCode;

    public MovementValidationException(List<String> errorMessages) {
        super(MOVEMENT_VALIDATION_ERROR.getMessage(errorMessages));
        this.errors = errorMessages;
        this.status = MOVEMENT_VALIDATION_ERROR.getStatus();
        this.errorCode = MOVEMENT_VALIDATION_ERROR.getErrorCode();
    }
}