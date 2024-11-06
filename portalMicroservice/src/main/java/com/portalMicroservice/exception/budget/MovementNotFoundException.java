package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.UUID;

import static com.portalMicroservice.exception.ErrorMessage.MOVEMENTS_NOT_FOUND_BETWEEN_DATES;
import static com.portalMicroservice.exception.ErrorMessage.MOVEMENT_NOT_FOUND;


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

    public MovementNotFoundException(LocalDate startDate, LocalDate endDate) {
        super(MOVEMENTS_NOT_FOUND_BETWEEN_DATES.getMessage(startDate, endDate));
        this.message = MOVEMENTS_NOT_FOUND_BETWEEN_DATES.getMessage(startDate, endDate);
        this.status = MOVEMENTS_NOT_FOUND_BETWEEN_DATES.getStatus();
        this.errorCode = MOVEMENTS_NOT_FOUND_BETWEEN_DATES.getErrorCode();
    }
}
