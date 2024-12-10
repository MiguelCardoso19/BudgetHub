package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.budgetMicroservice.exception.ErrorMessage.INVOICE_ALREADY_EXISTS;

@Getter
public class InvoiceAlreadyExistsException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public InvoiceAlreadyExistsException(String documentNumber) {
        super(INVOICE_ALREADY_EXISTS.getMessage(documentNumber));
        this.message = INVOICE_ALREADY_EXISTS.getMessage(documentNumber);
        this.status = INVOICE_ALREADY_EXISTS.getStatus();
        this.errorCode = INVOICE_ALREADY_EXISTS.getErrorCode();
    }
}
