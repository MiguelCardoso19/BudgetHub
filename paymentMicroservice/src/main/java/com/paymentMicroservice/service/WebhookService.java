package com.paymentMicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface WebhookService {
    String handleWebhookEvents( String payload, String sigHeader) throws JsonProcessingException, StripeException, ExecutionException, InterruptedException, TimeoutException;
}