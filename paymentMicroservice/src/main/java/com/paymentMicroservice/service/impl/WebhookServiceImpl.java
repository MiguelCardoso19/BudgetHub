package com.paymentMicroservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentMicroservice.dto.MovementDTO;
import com.paymentMicroservice.dto.MovementUpdateStatusRequestDTO;
import com.paymentMicroservice.enumerators.MovementType;
import com.paymentMicroservice.service.WebhookService;
import com.paymentMicroservice.util.WebhookUtils;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static com.paymentMicroservice.enumerators.MovementStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {
    private final KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;
    private final KafkaTemplate<String, MovementUpdateStatusRequestDTO> kafkaMovementUpdateStatusRequestTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @Value("${STRIPE_WEBHOOK_KEY}")
    private String STRIPE_WEBHOOK_KEY;

    @Override
    public String handleWebhookEvents(String payload, String sigHeader) throws JsonProcessingException {
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

    private void handleEvent(Event event, StripeObject stripeObject) throws JsonProcessingException {
        switch (event.getType()) {
            case "payment_intent.created" -> handlePaymentIntentCreated((PaymentIntent) stripeObject);
            case "payment_intent.succeeded" -> handlePaymentIntentSucceeded((PaymentIntent) stripeObject);
            case "charge.succeeded" -> handleChargeSucceeded((PaymentIntent) stripeObject);
            case "charge.updated" -> handleChargeUpdated((PaymentIntent) stripeObject);
            case "payment_intent.canceled" -> handlePaymentIntentCanceled((PaymentIntent) stripeObject);
            case "payment_method.attached" -> handlePaymentMethodAttached((PaymentMethod) stripeObject);
            default -> log.warn("Unhandled event type: {}", event.getType());
        }
    }

    private void handlePaymentIntentCreated(PaymentIntent paymentIntent) throws JsonProcessingException {
        log.info("Payment for intent {} created.", paymentIntent.getId());
        Map<String, String> metadata = paymentIntent.getMetadata();
        Map<String, UUID> parsedItems = WebhookUtils.parseItemsMetadata(metadata);

        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setDescription(metadata.get("description"));
        movementDTO.setDocumentNumber(paymentIntent.getId());
        movementDTO.setDateOfEmission(LocalDate.now());
        movementDTO.setType(MovementType.valueOf(metadata.get("movement_type")));
        movementDTO.setIvaRate(Double.parseDouble(metadata.get("iva_rate")));
        movementDTO.setValueWithoutIva(Double.parseDouble(metadata.get("total_amount")));
        movementDTO.setBudgetTypeId(parsedItems.get("budgetTypeId"));
        movementDTO.setBudgetSubtypeId(parsedItems.get("budgetSubtypeId"));
        movementDTO.setSupplierId(parsedItems.get("supplierId"));
        kafkaMovementTemplate.send("create-movement", movementDTO);
    }

    private void handlePaymentIntentCanceled(PaymentIntent paymentIntent) {
        log.info("Payment for {} canceled.", paymentIntent.getId());
        kafkaMovementUpdateStatusRequestTemplate.send("update-movement-status",
                WebhookUtils.buildMovementUpdateRequestDTO(CANCELED, paymentIntent.getId()));
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        log.info("Payment for intent {} succeeded.", paymentIntent.getId());
        kafkaMovementUpdateStatusRequestTemplate.send("update-movement-status",
                WebhookUtils.buildMovementUpdateRequestDTO(PROCESSING, paymentIntent.getId()));
    }

    private void handleChargeSucceeded(PaymentIntent paymentIntent) {
        log.info("Charge succeeded for amount: {}", paymentIntent.getId());
        kafkaMovementUpdateStatusRequestTemplate.send("update-movement-status",
                WebhookUtils.buildMovementUpdateRequestDTO(SUCCEEDED, paymentIntent.getId()));
    }

    private void handleChargeUpdated(PaymentIntent paymentIntent) {
        log.info("Charge updated with ID: {}", paymentIntent.getId());
    }

    private void handlePaymentMethodAttached(PaymentMethod paymentMethod) {
        log.info("Payment method attached with ID: {}", paymentMethod.getId());
    }
}