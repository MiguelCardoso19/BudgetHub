package com.paymentMicroservice.controller;

import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.PaymentConfirmationRequest;
import com.paymentMicroservice.exception.FailedToCancelPaymentException;
import com.paymentMicroservice.exception.FailedToConfirmPaymentException;
import com.paymentMicroservice.service.PaymentService;
import com.stripe.exception.StripeException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // String to make frontend happy
    @PostMapping("/create-payment-intent")
    public String createPaymentIntent(@Valid @RequestBody CreatePaymentDTO createPaymentDTO) throws StripeException {
        return paymentService.createPaymentIntent(createPaymentDTO);
    }

    @PostMapping("/confirm-payment-intent")
    public ResponseEntity<Void> confirmPaymentIntent(@RequestBody PaymentConfirmationRequest request) throws StripeException, FailedToConfirmPaymentException {
        paymentService.confirmPaymentIntent(request);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/cancel-payment-intent")
    public ResponseEntity<Void> cancelPaymentIntent(@RequestBody PaymentConfirmationRequest request) throws StripeException, FailedToCancelPaymentException {
        paymentService.cancelPaymentIntent(request);
        return ResponseEntity.ok().build();
    }
}