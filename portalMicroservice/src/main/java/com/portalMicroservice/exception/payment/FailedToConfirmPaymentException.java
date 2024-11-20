package com.portalMicroservice.exception.payment;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.FAILED_TO_CONFIRM_PAYMENT;

@Getter
public class FailedToConfirmPaymentException extends Exception {
    private final String correlationId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public FailedToConfirmPaymentException(String id) {
        super(FAILED_TO_CONFIRM_PAYMENT.getMessage());
        this.correlationId = id;
        this.message = FAILED_TO_CONFIRM_PAYMENT.getMessage();
        this.status = FAILED_TO_CONFIRM_PAYMENT.getStatus();
        this.errorCode = FAILED_TO_CONFIRM_PAYMENT.getErrorCode();
    }
}
