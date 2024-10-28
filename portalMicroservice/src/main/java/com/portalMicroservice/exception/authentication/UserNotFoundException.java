package com.portalMicroservice.exception.authentication;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.portalMicroservice.exception.ErrorMessage.USER_ID_NOT_FOUND;
import static com.portalMicroservice.exception.ErrorMessage.USER_NAME_NOT_FOUND;

@Getter
public class UserNotFoundException extends Exception {
    private final String message;
    private final String errorCode;
    private final HttpStatus status;

    public UserNotFoundException(UUID id) {
        super(USER_ID_NOT_FOUND.getMessage(id));
        this.message = USER_ID_NOT_FOUND.getMessage(id);
        this.errorCode = USER_ID_NOT_FOUND.getErrorCode();
        this.status = USER_ID_NOT_FOUND.getStatus();
    }

    public UserNotFoundException(String name) {
        super(USER_NAME_NOT_FOUND.getMessage(name));
        this.message = USER_NAME_NOT_FOUND.getMessage(name);
        this.errorCode = USER_NAME_NOT_FOUND.getErrorCode();
        this.status = USER_NAME_NOT_FOUND.getStatus();
    }
}