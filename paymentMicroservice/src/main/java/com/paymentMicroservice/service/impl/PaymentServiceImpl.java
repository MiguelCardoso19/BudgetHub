package com.paymentMicroservice.service.impl;

import com.google.gson.Gson;
import com.paymentMicroservice.dto.CreatePaymentResponseDTO;
import com.paymentMicroservice.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Gson gson = new Gson();

    @Value("${STRIPE_PRIVATE_KEY}")
    private String STRIPE_PRIVATE_KEY;

    @PostConstruct
    private void initStripe() {
        Stripe.apiKey = STRIPE_PRIVATE_KEY;
    }

    @Override
    public String createPaymentIntent(String body) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(15 * 100L)
                .setCurrency("eur")
                .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setIdempotencyKey(UUID.randomUUID().toString())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params, requestOptions);
        CreatePaymentResponseDTO paymentResponse = new CreatePaymentResponseDTO(paymentIntent.getClientSecret());
        return gson.toJson(paymentResponse);
    }
}