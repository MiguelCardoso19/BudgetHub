package com.paymentMicroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentMicroservice.service.WebhookService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    private final WebhookService webhookService;

    @PostMapping("/webhook")
    public void handleStripeEvents(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) throws JsonProcessingException, StripeException, ExecutionException, InterruptedException, TimeoutException {
        webhookService.handleWebhookEvents(payload, sigHeader);
    }
}