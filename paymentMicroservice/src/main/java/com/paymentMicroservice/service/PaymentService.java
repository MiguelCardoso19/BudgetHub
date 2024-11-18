package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.PaymentConfirmationRequest;
import com.paymentMicroservice.dto.StripeCardTokenDTO;
import com.paymentMicroservice.dto.StripeSepaTokenDTO;
import com.paymentMicroservice.exception.BudgetExceededException;
import com.paymentMicroservice.exception.FailedToCancelPaymentException;
import com.paymentMicroservice.exception.FailedToConfirmPaymentException;
import com.stripe.exception.StripeException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface PaymentService {
    String createPaymentIntent(CreatePaymentDTO createPaymentDTO) throws StripeException, BudgetExceededException, ExecutionException, InterruptedException, TimeoutException;
    void cancelPaymentIntent(PaymentConfirmationRequest request) throws StripeException, FailedToCancelPaymentException;
    void confirmPaymentIntent(PaymentConfirmationRequest request) throws StripeException, FailedToConfirmPaymentException;
    StripeCardTokenDTO createCardToken(StripeCardTokenDTO model) throws StripeException;
    StripeSepaTokenDTO createSepaToken(StripeSepaTokenDTO model) throws StripeException;
    void retryFailedPayments() throws StripeException;
}