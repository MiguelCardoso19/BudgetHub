package com.portalMicroservice.exception.notification;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.FAILED_TO_SEND_EMAIL;

@Getter
public class FailedToSendEmailException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public FailedToSendEmailException(String email) {
        super(FAILED_TO_SEND_EMAIL.getMessage(email));
        this.message = FAILED_TO_SEND_EMAIL.getMessage(email);
        this.status = FAILED_TO_SEND_EMAIL.getStatus();
        this.errorCode = FAILED_TO_SEND_EMAIL.getErrorCode();
    }
}