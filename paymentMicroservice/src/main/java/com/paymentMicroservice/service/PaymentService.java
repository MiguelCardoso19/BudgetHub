package com.paymentMicroservice.service;

import com.stripe.exception.StripeException;

public interface PaymentService {
    String createPaymentIntent(String body) throws StripeException;
}