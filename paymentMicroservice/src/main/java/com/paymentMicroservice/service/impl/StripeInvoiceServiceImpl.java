package com.paymentMicroservice.service.impl;

import com.paymentMicroservice.dto.InvoiceDTO;
import com.paymentMicroservice.dto.NotificationRequestDTO;
import com.paymentMicroservice.service.StripeInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StripeInvoiceServiceImpl implements StripeInvoiceService {
    private final KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;
    private final KafkaTemplate<String, NotificationRequestDTO> kafkaNotificationRequestTemplate;

    @Override
    public void sendChargeSucceededInvoice(String receiptUrl, String movementDocumentNumber, String email) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setDescription("Charge from Stripe succeeded");
        invoiceDTO.setDateOfEmission(LocalDate.now());
        invoiceDTO.setStripeReceiptUrl(receiptUrl);
        invoiceDTO.setMovementDocumentNumber(movementDocumentNumber);
        kafkaInvoiceTemplate.send("create-invoice", movementDocumentNumber, invoiceDTO);
        kafkaNotificationRequestTemplate.send("send-invoice-notification", new NotificationRequestDTO(email, receiptUrl));
    }

    @Override
    public void sendChargeRefundInvoice(String receiptUrl, String movementDocumentNumber, String email) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setDescription("Charge refund from Stripe succeeded");
        invoiceDTO.setStripeReceiptUrl(receiptUrl);
        kafkaInvoiceTemplate.send("update-invoice", movementDocumentNumber, invoiceDTO);
        kafkaNotificationRequestTemplate.send("send-invoice-notification", new NotificationRequestDTO(email, receiptUrl));
    }
}
