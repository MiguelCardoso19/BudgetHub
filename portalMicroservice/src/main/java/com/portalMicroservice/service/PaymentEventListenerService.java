package com.portalMicroservice.service;

import com.portalMicroservice.dto.payment.CreatePaymentResponseDTO;
import com.portalMicroservice.dto.payment.StripeCardTokenDTO;
import com.portalMicroservice.dto.payment.StripeSepaTokenDTO;
import com.portalMicroservice.exception.budget.BudgetExceededException;
import com.portalMicroservice.exception.payment.FailedToCancelPaymentException;
import com.portalMicroservice.exception.payment.FailedToConfirmPaymentException;

public interface PaymentEventListenerService {
    void handleFailedToCancelPaymentExceptionResponse(FailedToCancelPaymentException errorPayload);
    void handleFailedToConfirmPaymentExceptionResponse(FailedToConfirmPaymentException errorPayload);
    void handleSepaTokenResponse(StripeSepaTokenDTO response);
    void handleCardTokenResponse(StripeCardTokenDTO response);
    void handleSuccessRefundResponse(String id);
    void handleSuccessPaymentActionResponse(String id);
    void handleCreatePaymentResponse(CreatePaymentResponseDTO response);
    void handleBudgetExceededForPaymentExceptionResponse(BudgetExceededException errorPayload);
}
