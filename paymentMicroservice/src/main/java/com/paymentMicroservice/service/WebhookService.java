package com.paymentMicroservice.service;

public interface WebhookService {
    String handleWebhookEvents( String payload, String sigHeader);
}