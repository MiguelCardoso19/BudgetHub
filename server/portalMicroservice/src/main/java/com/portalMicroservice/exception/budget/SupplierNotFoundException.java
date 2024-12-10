package com.portalMicroservice.exception.budget;

import lombok.Data;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.SUPPLIER_NOT_FOUND;

@Data
public class SupplierNotFoundException extends Exception {
    private String id;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public SupplierNotFoundException(String id) {
        super(SUPPLIER_NOT_FOUND.getMessage(id));
        this.id = id;
        this.message = SUPPLIER_NOT_FOUND.getMessage(id);
        this.status = SUPPLIER_NOT_FOUND.getStatus();
        this.errorCode = SUPPLIER_NOT_FOUND.getErrorCode();
    }
}
