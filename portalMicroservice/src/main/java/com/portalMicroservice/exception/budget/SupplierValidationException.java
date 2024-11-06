package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.SUPPLIER_VALIDATION_ERROR;

@Getter
public class SupplierValidationException extends Exception {
    private final HttpStatus status;
    private final String errorCode;

    public SupplierValidationException(String errorMessages) {
        super(SUPPLIER_VALIDATION_ERROR.getMessage(errorMessages));
        this.status = SUPPLIER_VALIDATION_ERROR.getStatus();
        this.errorCode = SUPPLIER_VALIDATION_ERROR.getErrorCode();
    }
}
