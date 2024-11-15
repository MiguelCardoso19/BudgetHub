package com.paymentMicroservice.service.impl;

import com.google.gson.Gson;
import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.CreatePaymentResponseDTO;
import com.paymentMicroservice.dto.PaymentConfirmationRequest;
import com.paymentMicroservice.exception.FailedToCancelPaymentException;
import com.paymentMicroservice.exception.FailedToConfirmPaymentException;
import com.paymentMicroservice.service.PaymentService;
import com.paymentMicroservice.util.PaymentUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentConfirmParams;
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

    // String to make frontend happy
    @Override
    public String createPaymentIntent(CreatePaymentDTO createPaymentDTO) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(PaymentUtils.calculateAmount(createPaymentDTO) * 100L)
                .setReceiptEmail(createPaymentDTO.getReceiptEmail())
                //    .setCurrency(createPaymentDTO.getCurrency())
                .setCurrency("EUR")
                .setPaymentMethod(createPaymentDTO.getPaymentMethodId())
                .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setIdempotencyKey(UUID.randomUUID().toString())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params, requestOptions);
        CreatePaymentResponseDTO paymentResponse = new CreatePaymentResponseDTO(paymentIntent.getClientSecret(), paymentIntent.getId());
        return gson.toJson(paymentResponse);
    }

    @Override
    public void confirmPaymentIntent(PaymentConfirmationRequest request) throws FailedToConfirmPaymentException, StripeException {
        PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                .setReturnUrl("http://localhost:8084/complete.html")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getClientSecret());

        if ("canceled".equals(paymentIntent.getStatus())) {
            throw new FailedToConfirmPaymentException(paymentIntent.getId());
        }

        paymentIntent.confirm(confirmParams);
    }

    @Override
    public void cancelPaymentIntent(PaymentConfirmationRequest request) throws StripeException, FailedToCancelPaymentException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getClientSecret());

        if ("requires_payment_method".equals(paymentIntent.getStatus()) ||
                "requires_confirmation".equals(paymentIntent.getStatus())) {
            paymentIntent.cancel(PaymentIntentCancelParams.builder().build());
        } else {
            throw new FailedToCancelPaymentException(request.getClientSecret().substring(0, request.getClientSecret().indexOf("_")));
        }
    }
}