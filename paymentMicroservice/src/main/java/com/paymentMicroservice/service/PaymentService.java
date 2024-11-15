package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.PaymentConfirmationRequest;
import com.paymentMicroservice.exception.FailedToCancelPaymentException;
import com.paymentMicroservice.exception.FailedToConfirmPaymentException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    String createPaymentIntent(CreatePaymentDTO createPaymentDTO) throws StripeException;
    void cancelPaymentIntent(PaymentConfirmationRequest request) throws StripeException, FailedToCancelPaymentException;
    void confirmPaymentIntent(PaymentConfirmationRequest request) throws StripeException, FailedToConfirmPaymentException;
}