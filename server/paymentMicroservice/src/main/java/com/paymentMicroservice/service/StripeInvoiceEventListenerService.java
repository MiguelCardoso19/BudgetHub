package com.paymentMicroservice.service;

import com.paymentMicroservice.dto.MovementDTO;

public interface StripeInvoiceEventListenerService {
    void handleMovementResponse(MovementDTO movementDTO);
}