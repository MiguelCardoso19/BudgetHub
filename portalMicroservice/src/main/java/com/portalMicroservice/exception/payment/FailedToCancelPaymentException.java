package com.portalMicroservice.exception.payment;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.FAILED_TO_CANCEL_PAYMENT;

@Getter
public class FailedToCancelPaymentException extends Exception {
    private final String correlationId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public FailedToCancelPaymentException(String id) {
        super(FAILED_TO_CANCEL_PAYMENT.getMessage());
        this.correlationId = id;
        this.message = FAILED_TO_CANCEL_PAYMENT.getMessage();
        this.status = FAILED_TO_CANCEL_PAYMENT.getStatus();
        this.errorCode = FAILED_TO_CANCEL_PAYMENT.getErrorCode();
    }
}
