package com.paymentMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorMessage {
    FAILED_TO_CANCEL_PAYMENT("FAILED_TO_CANCEL_PAYMENT", "Failed to cancel the payment", CONFLICT),
    FAILED_TO_CONFIRM_PAYMENT("FAILED_TO_CONFIRM_PAYMENT", "Failed to confirm the payment", CONFLICT),
    REFUND_NOT_POSSIBLE("REFUND_NOT_POSSIBLE", "The refund was not possible", CONFLICT),
    FAILED_TO_CREATE_PAYMENT_SESSION("FAILED_TO_CREATE_PAYMENT_SESSION", "Failed to create the payment session due to the following error/s: %s", CONFLICT),
    UNABLE_TO_CREATE_STRIPE_CARD_TOKEN("UNABLE_TO_CREATE_STRIPE_CARD_TOKEN", "It was not possible to create Stripe CARD payment method token", CONFLICT),
    UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN("UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN", "It was not possible to create Stripe SEPA payment method token", CONFLICT),
    BUDGET_EXCEEDED("BUDGET_EXCEEDED", "Total value exceeds the available budget. Total value: %s, Available: %s", BAD_REQUEST);

    @Getter
    private final String errorCode;
    @Getter
    private final HttpStatus status;

    private final String message;

    ErrorMessage(String errorCode, String message, HttpStatus status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}