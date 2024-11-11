package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.budgetMicroservice.exception.ErrorMessage.SUPPLIER_NOT_FOUND;

@Getter
public class SupplierNotFoundException extends Exception {
    private UUID id;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public SupplierNotFoundException(UUID id) {
        super(SUPPLIER_NOT_FOUND.getMessage(id));
        this.id = id;
        this.message = SUPPLIER_NOT_FOUND.getMessage(id);
        this.status = SUPPLIER_NOT_FOUND.getStatus();
        this.errorCode = SUPPLIER_NOT_FOUND.getErrorCode();
    }
}
