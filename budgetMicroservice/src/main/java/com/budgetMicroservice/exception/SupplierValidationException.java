package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.budgetMicroservice.exception.ErrorMessage.SUPPLIER_VALIDATION_ERROR;

@Getter
public class SupplierValidationException extends Exception {
    private final List<String> errors;
    private final HttpStatus status;
    private final String errorCode;

    public SupplierValidationException(List<String> errorMessages) {
        super(SUPPLIER_VALIDATION_ERROR.getMessage(errorMessages));
        this.errors = errorMessages;
        this.status = SUPPLIER_VALIDATION_ERROR.getStatus();
        this.errorCode = SUPPLIER_VALIDATION_ERROR.getErrorCode();
    }
}
