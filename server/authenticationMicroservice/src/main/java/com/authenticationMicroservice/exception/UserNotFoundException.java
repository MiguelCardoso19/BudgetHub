package com.authenticationMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.authenticationMicroservice.exception.ErrorMessage.USER_ID_NOT_FOUND;

@Getter
public class UserNotFoundException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public UserNotFoundException(UUID id) {
        super(USER_ID_NOT_FOUND.getMessage(id));
        this.message = USER_ID_NOT_FOUND.getMessage(id);
        this.status = USER_ID_NOT_FOUND.getStatus();
        this.errorCode = USER_ID_NOT_FOUND.getErrorCode();
    }
}