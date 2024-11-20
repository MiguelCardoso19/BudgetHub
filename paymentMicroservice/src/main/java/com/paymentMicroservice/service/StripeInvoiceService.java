package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.MovementDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface StripeInvoiceService {
    void sendChargeSucceededInvoice(String receiptUrl, String movementDocumentNumber, String email);
    void sendChargeRefundInvoice(String receiptUrl, String movementDocumentNumber, String email) throws ExecutionException, InterruptedException, TimeoutException;
    CompletableFuture<MovementDTO> removePendingMovementRequestByDocumentNumber(String documentNumber);
}