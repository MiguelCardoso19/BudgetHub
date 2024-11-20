package com.paymentMicroservice.service.impl;

import com.paymentMicroservice.dto.MovementDTO;
import com.paymentMicroservice.service.StripeInvoiceEventListenerService;
import com.paymentMicroservice.service.StripeInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class StripeInvoiceEventListenerServiceImpl implements StripeInvoiceEventListenerService {
    private final StripeInvoiceService stripeInvoiceService;

    @Override
    @KafkaListener(topics = "movement-payment-response", groupId = "movement_payment_response_group", concurrency = "10", containerFactory = "movementKafkaListenerContainerFactory")
    public void handleMovementResponse(MovementDTO movementDTO) {
        CompletableFuture<MovementDTO> future = stripeInvoiceService.removePendingMovementRequestByDocumentNumber(movementDTO.getDocumentNumber());
        if (future != null) {
            future.complete(movementDTO);
        }
    }
}
