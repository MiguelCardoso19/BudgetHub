package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.*;
import com.paymentMicroservice.exception.*;
import com.stripe.exception.StripeException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface PaymentService {
    void createPaymentIntent(CreatePaymentDTO createPaymentDTO) throws StripeException, BudgetExceededException, ExecutionException, InterruptedException, TimeoutException;
    void cancelPaymentIntent(PaymentActionRequestDTO request) throws StripeException, FailedToCancelPaymentException;
    void confirmPaymentIntent(PaymentActionRequestDTO request) throws StripeException, FailedToConfirmPaymentException;
    void createCardToken(StripeCardTokenDTO model) throws StripeException, StripeCardTokenCreationException;
    void createSepaToken(StripeSepaTokenDTO model) throws StripeException, StripeSepaTokenCreationException;
    void retryFailedPayments() throws StripeException;
    void refundCharge(RefundChargeRequestDTO request) throws StripeException, RefundException;
    void createPaymentSession(SessionRequestDTO sessionRequestDTO) throws BudgetExceededException, ExecutionException, InterruptedException, TimeoutException, PaymentSessionException;
}