package com.paymentMicroservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paymentMicroservice.dto.MovementDTO;
import com.paymentMicroservice.dto.MovementUpdateStatusRequestDTO;
import com.paymentMicroservice.enumerators.MovementType;
import com.paymentMicroservice.service.StripeInvoiceService;
import com.paymentMicroservice.service.WebhookService;
import com.paymentMicroservice.util.WebhookUtils;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.paymentMicroservice.enumerators.MovementStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {
    private final StripeInvoiceService stripeInvoiceService;
    private final KafkaTemplate<String, MovementDTO> kafkaMovementTemplate;
    private final KafkaTemplate<String, MovementUpdateStatusRequestDTO> kafkaMovementUpdateStatusRequestTemplate;

    @Value("${STRIPE_WEBHOOK_KEY}")
    private String STRIPE_WEBHOOK_KEY;

    @Override
    public String handleWebhookEvents(String payload, String sigHeader) throws JsonProcessingException, StripeException, ExecutionException, InterruptedException, TimeoutException {
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

    private void handleEvent(Event event, StripeObject stripeObject) throws JsonProcessingException, StripeException, ExecutionException, InterruptedException, TimeoutException {
        switch (event.getType()) {
            case "payment_intent.created" -> handlePaymentIntentCreated((PaymentIntent) stripeObject);
            case "payment_intent.succeeded" -> handlePaymentIntentSucceeded((PaymentIntent) stripeObject);
            case "charge.succeeded" -> handleChargeSucceeded((Charge) stripeObject);
            case "charge.refunded" -> handleChargeRefund((Charge) stripeObject);
            case "charge.updated" -> handleChargeUpdated((Charge) stripeObject);
            case "payment_intent.canceled" -> handlePaymentIntentCanceled((PaymentIntent) stripeObject);
            case "payment_method.attached" -> handlePaymentMethodAttached((PaymentMethod) stripeObject);
            default -> log.warn("Unhandled event type: {}", event.getType());
        }
    }

    private void handlePaymentIntentCreated(PaymentIntent paymentIntent) throws JsonProcessingException, StripeException {
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

        if (parsedItems.containsKey("budgetTypeId")) {
            movementDTO.setBudgetTypeId(parsedItems.get("budgetTypeId"));
        } else {
            movementDTO.setBudgetSubtypeId(parsedItems.get("budgetSubtypeId"));
        }
        movementDTO.setSupplierId(parsedItems.get("supplierId"));
        kafkaMovementTemplate.send("create-movement", paymentIntent.getId(), movementDTO);
    }

    private void handlePaymentIntentCanceled(PaymentIntent paymentIntent) {
        log.info("Payment intent {} canceled.", paymentIntent.getId());
        kafkaMovementUpdateStatusRequestTemplate.send("update-movement-status", paymentIntent.getId(),
                WebhookUtils.buildMovementUpdateRequestDTO(CANCELED, paymentIntent.getId()));
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        log.info("Payment intent {} succeeded.", paymentIntent.getId());
        kafkaMovementUpdateStatusRequestTemplate.send("update-movement-status", paymentIntent.getId(),
                WebhookUtils.buildMovementUpdateRequestDTO(PROCESSING, paymentIntent.getId()));
    }

    private void handleChargeSucceeded(Charge charge) {
        log.info("Charge of payment intent {} succeeded ", charge.getPaymentIntent());
        stripeInvoiceService.sendChargeSucceededInvoice(charge.getReceiptUrl(), charge.getPaymentIntent(), charge.getReceiptEmail());
        kafkaMovementUpdateStatusRequestTemplate.send("update-movement-status", charge.getPaymentIntent(),
                WebhookUtils.buildMovementUpdateRequestDTO(SUCCEEDED, charge.getPaymentIntent()));
    }

    private void handleChargeRefund(Charge charge) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("Charge refunded with ID: {}", charge.getPaymentIntent());
        stripeInvoiceService.sendChargeRefundInvoice(charge.getReceiptUrl(), charge.getPaymentIntent(), charge.getReceiptEmail());
        kafkaMovementUpdateStatusRequestTemplate.send("update-movement-status", charge.getPaymentIntent(),
                WebhookUtils.buildMovementUpdateRequestDTO(REFUNDED, charge.getPaymentIntent()));
    }

    private void handleChargeUpdated(Charge charge) {
        log.info("Charge updated with ID: {}", charge.getPaymentIntent());
    }

    private void handlePaymentMethodAttached(PaymentMethod paymentMethod) {
        log.info("Payment method attached with ID: {}", paymentMethod.getId());
    }
}