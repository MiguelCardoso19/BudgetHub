package com.paymentMicroservice.util;

import com.google.gson.Gson;
import com.paymentMicroservice.dto.CreatePaymentDTO;
import com.paymentMicroservice.dto.CreatePaymentItemDTO;
import com.paymentMicroservice.dto.SessionRequestDTO;
import com.paymentMicroservice.model.FailedPayment;
import com.paymentMicroservice.repository.FailedPaymentRepository;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PaymentUtils {

    private static final Gson gson = new Gson();

    public static long calculateAmount(CreatePaymentItemDTO[] createPaymentItemDTO) {
        long amount = 0;

        for (CreatePaymentItemDTO item : createPaymentItemDTO) {
            amount += (long) (item.getAmount() * (1 + item.getIvaRate()));
        }

        return amount;
    }

    public static Map<String, String> buildMetadata(Object dto) {
        double[] ivaRate = new double[1];
        ivaRate[0] = 0.0;
        Map<String, String> metadata = new HashMap<>();
        Map<String, Map<String, String>> itemsMetadata = new HashMap<>();

        if (dto instanceof CreatePaymentDTO createPaymentDTO) {
            extractItemsMetadata(createPaymentDTO.getItems(), itemsMetadata, ivaRate);
            metadata.put("movement_type", createPaymentDTO.getMovementType().toString());
            metadata.put("total_amount", String.valueOf(calculateAmount(createPaymentDTO.getItems())));
            metadata.put("iva_rate", Double.toString(ivaRate[0]));
            metadata.put("description", createPaymentDTO.getDescription());
            metadata.put("receipt_email", createPaymentDTO.getReceiptEmail());
            metadata.put("currency", createPaymentDTO.getCurrency());
            metadata.put("items", gson.toJson(itemsMetadata));

        } else if (dto instanceof SessionRequestDTO sessionRequestDTO) {
            extractItemsMetadata(sessionRequestDTO.getItems(), itemsMetadata, ivaRate);
            metadata.put("total_amount", String.valueOf(calculateAmount(sessionRequestDTO.getItems())));
            metadata.put("iva_rate", Double.toString(ivaRate[0]));
            metadata.put("currency", sessionRequestDTO.getCurrency());
            metadata.put("description", sessionRequestDTO.getDescription());
            metadata.put("receipt_email", sessionRequestDTO.getEmail());
            metadata.put("items", gson.toJson(itemsMetadata));
        }

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

    public static SessionCreateParams.Builder initializeSessionBuilder(Customer customer) {
        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomer(customer.getId())
                .setSuccessUrl("https://localhost:4200/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://localhost:4200/cancel")
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.SEPA_DEBIT)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.MULTIBANCO)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.REVOLUT_PAY)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL);
    }

    public static void addLineItemsToSession(SessionRequestDTO sessionRequestDTO, SessionCreateParams.Builder sessionBuilder) {
        for (CreatePaymentItemDTO item : sessionRequestDTO.getItems()) {
            SessionCreateParams.LineItem.PriceData.ProductData productData = buildProductData(item);
            SessionCreateParams.LineItem.PriceData priceData = buildPriceData(item, sessionRequestDTO, productData);

            sessionBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(priceData)
                            .build()
            );
        }
    }

    public static RequestOptions setIdempotencyKey(String idempotencyKey) {
        return RequestOptions.builder()
                .setIdempotencyKey(idempotencyKey)
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData.ProductData buildProductData(CreatePaymentItemDTO item) {
        SessionCreateParams.LineItem.PriceData.ProductData.Builder productDataBuilder =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .putMetadata("supplier_id", item.getSupplierId().toString())
                        .setName(item.getId());

        if (item.getBudgetTypeId() != null) {
            productDataBuilder.putMetadata("budgetType_id", item.getBudgetTypeId().toString());
        }
        if (item.getBudgetSubtypeId() != null) {
            productDataBuilder.putMetadata("budgetSubtype_id", item.getBudgetSubtypeId().toString());
        }

        return productDataBuilder.build();
    }

    private static SessionCreateParams.LineItem.PriceData buildPriceData(CreatePaymentItemDTO item, SessionRequestDTO sessionRequestDTO,
                                                                         SessionCreateParams.LineItem.PriceData.ProductData productData) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setProductData(productData)
                .setCurrency(sessionRequestDTO.getCurrency())
                .setUnitAmountDecimal(BigDecimal.valueOf((item.getAmount() * (1 + item.getIvaRate())) * 100))
                .build();
    }

    private static void extractItemsMetadata(CreatePaymentItemDTO[] items, Map<String, Map<String, String>> itemsMetadata, double[] ivaRate) {
        for (CreatePaymentItemDTO item : items) {
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

            ivaRate[0] = item.getIvaRate();
            itemsMetadata.put(item.getId(), itemData);
        }
    }
}