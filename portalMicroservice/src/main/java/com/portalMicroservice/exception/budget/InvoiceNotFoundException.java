package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.INVOICE_NOT_FOUND;

@Getter
public class InvoiceNotFoundException extends Exception {
    private String id;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public InvoiceNotFoundException(String id) {
        super(INVOICE_NOT_FOUND.getMessage(id));
        this.id = id;
        this.message = INVOICE_NOT_FOUND.getMessage(id);
        this.status = INVOICE_NOT_FOUND.getStatus();
        this.errorCode = INVOICE_NOT_FOUND.getErrorCode();
    }
}