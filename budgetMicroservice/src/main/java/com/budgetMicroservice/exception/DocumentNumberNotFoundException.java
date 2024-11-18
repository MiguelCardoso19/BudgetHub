package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.budgetMicroservice.exception.ErrorMessage.DOCUMENT_NUMBER_NOT_FOUND;

@Getter
public class DocumentNumberNotFoundException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public DocumentNumberNotFoundException (String documentNumber) {
        super(DOCUMENT_NUMBER_NOT_FOUND.getMessage(documentNumber));
        this.message = DOCUMENT_NUMBER_NOT_FOUND.getMessage(documentNumber);
        this.status = DOCUMENT_NUMBER_NOT_FOUND.getStatus();
        this.errorCode = DOCUMENT_NUMBER_NOT_FOUND.getErrorCode();
    }
}
