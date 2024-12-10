package com.portalMicroservice.exception.payment;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.REFUND_NOT_POSSIBLE;

@Getter
public class RefundException extends Exception {
    private String correlationId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public RefundException(String correlationId) {
        super(REFUND_NOT_POSSIBLE.getMessage());
        this.correlationId = correlationId;
        this.message = REFUND_NOT_POSSIBLE.getMessage();
        this.status = REFUND_NOT_POSSIBLE.getStatus();
        this.errorCode = REFUND_NOT_POSSIBLE.getErrorCode();
    }

    public RefundException() {
        super(REFUND_NOT_POSSIBLE.getMessage());
        this.message = REFUND_NOT_POSSIBLE.getMessage();
        this.status = REFUND_NOT_POSSIBLE.getStatus();
        this.errorCode = REFUND_NOT_POSSIBLE.getErrorCode();
    }
}