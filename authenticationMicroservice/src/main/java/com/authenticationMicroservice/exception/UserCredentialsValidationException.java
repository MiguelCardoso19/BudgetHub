package com.authenticationMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.authenticationMicroservice.exception.ErrorMessage.USER_CREDENTIALS_VALIDATION_ERROR;

@Getter
public class UserCredentialsValidationException extends Exception {
    private final List<String> errors;
    private final HttpStatus status;
    private final String errorCode;

    public UserCredentialsValidationException(List<String> errorMessages) {
        super(USER_CREDENTIALS_VALIDATION_ERROR.getMessage(errorMessages));
        this.errors = errorMessages;
        this.status = USER_CREDENTIALS_VALIDATION_ERROR.getStatus();
        this.errorCode = USER_CREDENTIALS_VALIDATION_ERROR.getErrorCode();
    }
}