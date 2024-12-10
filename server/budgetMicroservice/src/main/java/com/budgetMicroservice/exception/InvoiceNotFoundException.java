package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.budgetMicroservice.exception.ErrorMessage.INVOICE_NOT_FOUND;

@Getter
public class InvoiceNotFoundException extends Exception {
    private UUID id;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public InvoiceNotFoundException(UUID id) {
        super(INVOICE_NOT_FOUND.getMessage(id));
        this.id = id;
        this.message = INVOICE_NOT_FOUND.getMessage(id);
        this.status = INVOICE_NOT_FOUND.getStatus();
        this.errorCode = INVOICE_NOT_FOUND.getErrorCode();
    }
}