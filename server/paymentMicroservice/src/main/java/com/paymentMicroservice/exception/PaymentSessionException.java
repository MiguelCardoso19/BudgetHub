package com.paymentMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.paymentMicroservice.exception.ErrorMessage.FAILED_TO_CREATE_PAYMENT_SESSION;

@Getter
public class PaymentSessionException extends Exception {
    private UUID correlationId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public PaymentSessionException(String message, UUID id) {
        super(FAILED_TO_CREATE_PAYMENT_SESSION.getMessage(message));
        this.correlationId = id;
        this.message = FAILED_TO_CREATE_PAYMENT_SESSION.getMessage(message);
        this.status = FAILED_TO_CREATE_PAYMENT_SESSION.getStatus();
        this.errorCode = FAILED_TO_CREATE_PAYMENT_SESSION.getErrorCode();
    }

    public PaymentSessionException(String message) {
        super(FAILED_TO_CREATE_PAYMENT_SESSION.getMessage(message));
        this.message = FAILED_TO_CREATE_PAYMENT_SESSION.getMessage(message);
        this.status = FAILED_TO_CREATE_PAYMENT_SESSION.getStatus();
        this.errorCode = FAILED_TO_CREATE_PAYMENT_SESSION.getErrorCode();
    }
}
