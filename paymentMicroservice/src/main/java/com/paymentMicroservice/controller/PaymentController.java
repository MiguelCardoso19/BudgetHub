package com.paymentMicroservice.controller;

import com.paymentMicroservice.service.PaymentService;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public String createPaymentIntent(@RequestBody String body) throws StripeException {
        return paymentService.createPaymentIntent(body);
    }
}