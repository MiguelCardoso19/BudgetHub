package com.paymentMicroservice.service.impl;

import com.paymentMicroservice.dto.MovementDTO;
import com.paymentMicroservice.service.WebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {
    private final KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;

    @Value("${STRIPE_WEBHOOK_KEY}")
    private String STRIPE_WEBHOOK_KEY;

    @Override
    public String handleWebhookEvents(String payload, String sigHeader) {
        if (sigHeader == null) {
            log.warn("Missing signature header.");
            return "";
        }

        Event event = validateEvent(payload, sigHeader);
        if (event == null) return "";

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = deserializer.getObject().orElse(null);

        if (stripeObject == null) {
            log.warn("Failed to deserialize Stripe object for event: {}", event.getType());
            return "";
        }

        handleEvent(event, stripeObject);
        return "";
    }

    private Event validateEvent(String payload, String sigHeader) {
        try {
            return Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_KEY);
        } catch (SignatureVerificationException e) {
            log.warn("Webhook error while validating signature.");
            return null;
        }
    }

    private void handleEvent(Event event, StripeObject stripeObject) {
        switch (event.getType()) {
            case "payment_intent.created" -> handlePaymentIntentCreated((PaymentIntent) stripeObject);
            case "payment_intent.succeeded" -> handlePaymentIntentSucceeded((PaymentIntent) stripeObject);
            case "charge.succeeded" -> handleChargeSucceeded((Charge) stripeObject);
            case "charge.updated" -> handleChargeUpdated((Charge) stripeObject);
            case "payment_intent.canceled" -> handlePaymentIntentCanceled((PaymentIntent) stripeObject);
            case "payment_method.attached" -> handlePaymentMethodAttached((PaymentMethod) stripeObject);
            default -> log.warn("Unhandled event type: {}", event.getType());
        }
    }

    private void handlePaymentIntentCanceled(PaymentIntent  paymentIntent) {
        log.info("Payment for {} canceled.", paymentIntent.getAmount());
    }

    private void handlePaymentIntentCreated(PaymentIntent paymentIntent) {
        log.info("PaymentIntent created with ID: {}", paymentIntent.getId());
        kafkaMovementTemplate.send("create-movement", new MovementDTO());
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        log.info("Payment for {} succeeded.", paymentIntent.getAmount());
    }

    private void handleChargeSucceeded(Charge charge) {
        log.info("Charge succeeded for amount: {}", charge.getAmount());
    }

    private void handleChargeUpdated(Charge charge) {
        log.info("Charge updated with ID: {}", charge.getId());
    }

    private void handlePaymentMethodAttached(PaymentMethod paymentMethod) {
        log.info("Payment method attached with ID: {}", paymentMethod.getId());
    }
}
