package com.paymentMicroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentMicroservice.service.WebhookService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhook")
@Tag(name = "Webhook Controller", description = "Handles Stripe webhook events")
public class WebhookController {

    private final WebhookService webhookService;

    @Operation(summary = "Handle Stripe webhook events",
            description = "Processes events received from Stripe Webhooks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event successfully processed"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or missing Stripe signature"),
            @ApiResponse(responseCode = "500", description = "Internal server error during event processing")
    })
    @PostMapping("/stripe")
    public void handleStripeEvents(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) throws JsonProcessingException, StripeException, ExecutionException, InterruptedException, TimeoutException {
        webhookService.handleWebhookEvents(payload, sigHeader);
    }
}