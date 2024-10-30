package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.budgetMicroservice.exception.ErrorMessage.MOVEMENT_NOT_FOUND;

@Getter
public class MovementNotFoundException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public MovementNotFoundException(UUID id) {
        super(MOVEMENT_NOT_FOUND.getMessage(id));
        this.message = MOVEMENT_NOT_FOUND.getMessage(id);
        this.status = MOVEMENT_NOT_FOUND.getStatus();
        this.errorCode = MOVEMENT_NOT_FOUND.getErrorCode();
    }
}
