package com.portalMicroservice.service;

import com.portalMicroservice.dto.payment.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface PaymentService {
    CreatePaymentResponseDTO createPaymentIntent(CreatePaymentDTO createPaymentDTO, String receiptEmail) throws ExecutionException, InterruptedException, TimeoutException;
    void cancelPayment(PaymentActionRequestDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException;
    void confirmPayment(PaymentActionRequestDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException;
    void refundCharge(RefundChargeRequestDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException;
    StripeCardTokenDTO createCardToken(StripeCardTokenDTO model, String email) throws ExecutionException, InterruptedException, TimeoutException;
    StripeSepaTokenDTO createSepaToken(StripeSepaTokenDTO model, String email) throws ExecutionException, InterruptedException, TimeoutException;
    SessionResponseDTO createPaymentSession(SessionRequestDTO request, String email) throws ExecutionException, InterruptedException, TimeoutException;
    CompletableFuture<SessionResponseDTO> removePendingCreatePaymentSessionRequestByCorrelationId(UUID correlationId);
    CompletableFuture<CreatePaymentResponseDTO> removePendingCreatePaymentIntentRequestByCorrelationId(UUID correlationId);
    CompletableFuture<PaymentActionRequestDTO> removePendingActionRequestByIntentId(String id);
    CompletableFuture<RefundChargeRequestDTO> removePendingRefundRequestByIntentId(String id);
    CompletableFuture<StripeCardTokenDTO> removePendingCardTokenRequestByCorrelationId(UUID correlationId);
    CompletableFuture<StripeSepaTokenDTO> removePendingSepaTokenRequestByCorrelationId(UUID correlationId);
}
