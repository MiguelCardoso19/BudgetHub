package com.paymentMicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface WebhookService {
    String handleWebhookEvents( String payload, String sigHeader) throws JsonProcessingException;
}