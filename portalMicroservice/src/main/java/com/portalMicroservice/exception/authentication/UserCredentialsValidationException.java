package com.portalMicroservice.exception.authentication;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.USER_CREDENTIALS_VALIDATION_ERROR;

@Getter
public class UserCredentialsValidationException extends Exception {
    private final String errors;
    private final String errorCode;
    private final HttpStatus status;

    public UserCredentialsValidationException(String errorMessage) {
        super(USER_CREDENTIALS_VALIDATION_ERROR.getMessage(errorMessage));
        this.errors = errorMessage;
        this.errorCode = USER_CREDENTIALS_VALIDATION_ERROR.getErrorCode();
        this.status = USER_CREDENTIALS_VALIDATION_ERROR.getStatus();
    }
}