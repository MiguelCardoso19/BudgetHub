package com.portalMicroservice.exception.payment;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.portalMicroservice.exception.ErrorMessage.UNABLE_TO_CREATE_STRIPE_CARD_TOKEN;

@Getter
public class StripeCardTokenCreationException extends Exception {
    private UUID correlationId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public StripeCardTokenCreationException(UUID id) {
        super(UNABLE_TO_CREATE_STRIPE_CARD_TOKEN.getMessage());
        this.correlationId = id;
        this.message = UNABLE_TO_CREATE_STRIPE_CARD_TOKEN.getMessage();
        this.status = UNABLE_TO_CREATE_STRIPE_CARD_TOKEN.getStatus();
        this.errorCode = UNABLE_TO_CREATE_STRIPE_CARD_TOKEN.getErrorCode();
    }

    public StripeCardTokenCreationException() {
        super(UNABLE_TO_CREATE_STRIPE_CARD_TOKEN.getMessage());
        this.message = UNABLE_TO_CREATE_STRIPE_CARD_TOKEN.getMessage();
        this.status = UNABLE_TO_CREATE_STRIPE_CARD_TOKEN.getStatus();
        this.errorCode = UNABLE_TO_CREATE_STRIPE_CARD_TOKEN.getErrorCode();
    }
}
