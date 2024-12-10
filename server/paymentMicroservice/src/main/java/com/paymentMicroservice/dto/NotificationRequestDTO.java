package com.paymentMicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Data Transfer Object representing a notification request, used for sending notifications via email.")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    @Schema(description = "Email address of the notification recipient",
            example = "recipient@example.com", required = true)
    private String recipient;

    @Schema(description = "A URL link to the Stripe receipt, allowing users to view or download their receipt.",
            example = "https://stripe.com/receipts/example123",
            nullable = true)
    private String stripeReceiptUrl;
}
