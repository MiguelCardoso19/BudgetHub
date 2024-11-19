package com.paymentMicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;

public interface WebhookService {
    String handleWebhookEvents( String payload, String sigHeader) throws JsonProcessingException, StripeException;
}