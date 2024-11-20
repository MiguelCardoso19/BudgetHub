//package com.paymentMicroservice.controller;
//
//import com.paymentMicroservice.dto.*;
//import com.paymentMicroservice.exception.BudgetExceededException;
//import com.paymentMicroservice.exception.FailedToCancelPaymentException;
//import com.paymentMicroservice.exception.FailedToConfirmPaymentException;
//import com.paymentMicroservice.service.PaymentService;
//import com.stripe.exception.StripeException;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeoutException;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/invoice")
//public class PaymentController {
//    private final PaymentService paymentService;
//
//    @PostMapping("/create-payment-intent")
//    public ResponseEntity<CreatePaymentResponseDTO> createPaymentIntent(@Valid @RequestBody CreatePaymentDTO createPaymentDTO) throws StripeException, BudgetExceededException, ExecutionException, InterruptedException, TimeoutException {
//        return ResponseEntity.ok(paymentService.createPaymentIntent(createPaymentDTO));
//    }
//
//    @PostMapping("/confirm-payment-intent")
//    public ResponseEntity<Void> confirmPaymentIntent(@Valid @RequestBody PaymentActionRequestDTO request) throws StripeException, FailedToConfirmPaymentException {
//        paymentService.confirmPaymentIntent(request);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/cancel-payment-intent")
//    public ResponseEntity<Void> cancelPaymentIntent(@Valid @RequestBody PaymentActionRequestDTO request) throws StripeException, FailedToCancelPaymentException {
//        paymentService.cancelPaymentIntent(request);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/refund-charge")
//    public ResponseEntity<Void> refundCharge(@Valid @RequestBody RefundChargeRequestDTO request) throws StripeException {
//        paymentService.refundCharge(request);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/create-card-token")
//    public ResponseEntity<Void> createCardToken(@Valid @RequestBody StripeCardTokenDTO model) throws StripeException {
//        paymentService.createCardToken(model);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/create-sepa-token")
//    public ResponseEntity<Void> createSepaToken(@Valid @RequestBody StripeSepaTokenDTO model) throws StripeException {
//        paymentService.createSepaToken(model);
//        return ResponseEntity.ok().build();
//    }
//}