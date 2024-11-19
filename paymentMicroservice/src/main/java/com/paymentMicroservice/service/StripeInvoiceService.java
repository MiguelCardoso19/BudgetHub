package com.paymentMicroservice.service;

public interface StripeInvoiceService {
    void sendChargeSucceededInvoice(String receiptUrl, String movementDocumentNumber, String email);
    void sendChargeRefundInvoice(String receiptUrl, String movementDocumentNumber, String email);
}