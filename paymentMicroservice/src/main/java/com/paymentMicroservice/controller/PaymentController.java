package com.paymentMicroservice.controller;

import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.PaymentConfirmationRequestDTO;
import com.paymentMicroservice.dto.RefundChargeRequestDTO;
import com.paymentMicroservice.exception.BudgetExceededException;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController()
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // String to make frontend happy
    @PostMapping("/create-payment-intent")
    public String createPaymentIntent(@Valid @RequestBody CreatePaymentDTO createPaymentDTO) throws StripeException, BudgetExceededException, ExecutionException, InterruptedException, TimeoutException {
        return paymentService.createPaymentIntent(createPaymentDTO);
    }

    @PostMapping("/confirm-payment-intent")
    public ResponseEntity<Void> confirmPaymentIntent(@RequestBody PaymentConfirmationRequestDTO request) throws StripeException, FailedToConfirmPaymentException {
        paymentService.confirmPaymentIntent(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel-payment-intent")
    public ResponseEntity<Void> cancelPaymentIntent(@RequestBody PaymentConfirmationRequestDTO request) throws StripeException, FailedToCancelPaymentException {
        paymentService.cancelPaymentIntent(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refund-charge")
    public ResponseEntity<Void> refundCharge(@RequestBody RefundChargeRequestDTO request) throws StripeException {
        paymentService.refundCharge(request);
        return ResponseEntity.ok().build();
    }
}