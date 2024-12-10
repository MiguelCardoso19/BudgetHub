package com.portalMicroservice.exception.payment;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.portalMicroservice.exception.ErrorMessage.UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN;


@Getter
public class StripeSepaTokenCreationException extends Exception {
    private UUID correlationId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public StripeSepaTokenCreationException(UUID id) {
        super(UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN.getMessage());
        this.correlationId = id;
        this.message = UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN.getMessage();
        this.status = UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN.getStatus();
        this.errorCode = UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN.getErrorCode();
    }

    public StripeSepaTokenCreationException() {
        super(UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN.getMessage());
        this.message = UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN.getMessage();
        this.status = UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN.getStatus();
        this.errorCode = UNABLE_TO_CREATE_STRIPE_SEPA_TOKEN.getErrorCode();
    }
}