package com.portalMicroservice.exception.budget;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.portalMicroservice.exception.ErrorMessage.SUPPLIER_VALIDATION_ERROR;

@Data
public class SupplierValidationException extends Exception {
    private UUID id;
    private final HttpStatus status;
    private final String errorCode;

    public SupplierValidationException(String errorMessages, UUID id) {
        super(SUPPLIER_VALIDATION_ERROR.getMessage(errorMessages));
        this.id = id;
        this.status = SUPPLIER_VALIDATION_ERROR.getStatus();
        this.errorCode = SUPPLIER_VALIDATION_ERROR.getErrorCode();
    }

    public SupplierValidationException(String errorMessages) {
        super(SUPPLIER_VALIDATION_ERROR.getMessage(errorMessages));
        this.status = SUPPLIER_VALIDATION_ERROR.getStatus();
        this.errorCode = SUPPLIER_VALIDATION_ERROR.getErrorCode();
    }
}
