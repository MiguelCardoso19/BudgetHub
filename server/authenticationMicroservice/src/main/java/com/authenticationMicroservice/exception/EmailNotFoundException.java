package com.authenticationMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.authenticationMicroservice.exception.ErrorMessage.EMAIL_NOT_FOUND;

@Getter
public class EmailNotFoundException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public EmailNotFoundException(String email) {
        super(EMAIL_NOT_FOUND.getMessage(email));
        this.message = EMAIL_NOT_FOUND.getMessage(email);
        this.status = EMAIL_NOT_FOUND.getStatus();
        this.errorCode = EMAIL_NOT_FOUND.getErrorCode();
    }
}