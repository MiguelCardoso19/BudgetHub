package com.paymentMicroservice.util;

import com.google.gson.Gson;
import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.CreatePaymentItemDTO;
import com.paymentMicroservice.model.FailedPayment;
import com.paymentMicroservice.repository.FailedPaymentRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PaymentUtils {

    private static final Gson gson = new Gson();

    public static long calculateAmount(CreatePaymentDTO createPaymentDTO) {
        long amount = 0;

        for (CreatePaymentItemDTO item : createPaymentDTO.getItems()) {
            amount += item.getAmount();
        }

        return amount;
    }

    public static Map<String, String> buildMetadata(CreatePaymentDTO createPaymentDTO) {
        double ivaRate = 0.0;
        Map<String, String> metadata = new HashMap<>();

        Map<String, Map<String, String>> itemsMetadata = new HashMap<>();
        for (CreatePaymentItemDTO item : createPaymentDTO.getItems()) {
            Map<String, String> itemData = new HashMap<>();
            itemData.put("amount", item.getAmount().toString());
            itemData.put("supplierId", item.getSupplierId().toString());
            itemData.put("ivaRate", item.getIvaRate().toString());

            if (item.getBudgetTypeId() != null) {
                itemData.put("budgetTypeId", item.getBudgetTypeId().toString());
            }

            if (item.getBudgetSubtypeId() != null && item.getBudgetTypeId() == null) {
                itemData.put("budgetSubtypeId", item.getBudgetSubtypeId().toString());
            }

            ivaRate = item.getIvaRate();
            itemsMetadata.put(item.getId(), itemData);
        }

        metadata.put("movement_type", createPaymentDTO.getMovementType().toString());
        metadata.put("total_amount", String.valueOf(calculateAmount(createPaymentDTO)));
        metadata.put("iva_rate", Double.toString(ivaRate));
        metadata.put("description", createPaymentDTO.getDescription());
        metadata.put("receipt_email", createPaymentDTO.getReceiptEmail());
        metadata.put("currency", createPaymentDTO.getCurrency());
        metadata.put("items", gson.toJson(itemsMetadata));
        return metadata;
    }

    public static void saveFailedPayment(String clientSecret, FailedPaymentRepository failedPaymentRepository) {
        FailedPayment failedPayment = new FailedPayment();
        failedPayment.setClientSecret(clientSecret);
        failedPayment.setRetryAttempts(0);
        failedPayment.setLastAttemptTime(LocalDateTime.now());
        failedPayment.setRetryable(true);
        failedPaymentRepository.save(failedPayment);
    }
}