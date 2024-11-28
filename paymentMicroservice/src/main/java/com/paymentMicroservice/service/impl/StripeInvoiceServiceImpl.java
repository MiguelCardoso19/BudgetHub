package com.paymentMicroservice.service.impl;

import com.paymentMicroservice.dto.*;
import com.paymentMicroservice.service.StripeInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class StripeInvoiceServiceImpl implements StripeInvoiceService {
    private final KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;
    private final KafkaTemplate<String, NotificationRequestDTO> kafkaNotificationRequestTemplate;
    private final KafkaTemplate<String, String> kafkaStringTemplate;

    private final ConcurrentHashMap<String, CompletableFuture<MovementDTO>> pendingMovementRequests = new ConcurrentHashMap<>();

    @Value("${TIMEOUT_DURATION}")
    private long TIMEOUT_DURATION;

    @Override
    public void sendChargeSucceededInvoice(String receiptUrl, String movementDocumentNumber, String email) {
        kafkaInvoiceTemplate.send("create-invoice", movementDocumentNumber, createInvoiceDTO("Charge from Stripe succeeded", receiptUrl, movementDocumentNumber));
        kafkaNotificationRequestTemplate.send("notification-stripe-receipt", new NotificationRequestDTO(email, receiptUrl));
    }

    @Override
    public void sendChargeRefundInvoice(String receiptUrl, String movementDocumentNumber, String email) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingMovementRequests.put(movementDocumentNumber, future);
        kafkaStringTemplate.send("get-movement-by-document-number", movementDocumentNumber, movementDocumentNumber);
        MovementDTO movementDTO = future.get(TIMEOUT_DURATION, SECONDS);
        InvoiceDTO invoiceDTO = createInvoiceDTO("Charge refund from Stripe succeeded", receiptUrl, movementDocumentNumber);
        invoiceDTO.setId(movementDTO.getInvoice().getId());
        kafkaInvoiceTemplate.send("update-invoice", movementDocumentNumber, invoiceDTO);
        kafkaNotificationRequestTemplate.send("notification-stripe-receipt", new NotificationRequestDTO(email, receiptUrl));
    }

    @Override
    public CompletableFuture<MovementDTO> removePendingMovementRequestByDocumentNumber(String documentNumber) {
        return pendingMovementRequests.remove(documentNumber);
    }

    private InvoiceDTO createInvoiceDTO(String description, String receiptUrl, String movementDocumentNumber) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setDescription(description);
        invoiceDTO.setDateOfEmission(LocalDate.now());
        invoiceDTO.setStripeReceiptUrl(receiptUrl);
        invoiceDTO.setMovementDocumentNumber(movementDocumentNumber);
        return invoiceDTO;
    }
}