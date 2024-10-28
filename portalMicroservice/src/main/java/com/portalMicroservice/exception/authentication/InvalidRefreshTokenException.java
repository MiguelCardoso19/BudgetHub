package com.portalMicroservice.exception.authentication;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.INVALID_REFRESH_TOKEN;

@Getter
public class InvalidRefreshTokenException extends Exception {
    private final String message;
    private final String errorCode;
    private final HttpStatus status;

    public InvalidRefreshTokenException() {
        super(INVALID_REFRESH_TOKEN.getMessage());
        this.message = INVALID_REFRESH_TOKEN.getMessage();
        this.errorCode = INVALID_REFRESH_TOKEN.getErrorCode();
        this.status = INVALID_REFRESH_TOKEN.getStatus();
    }
}