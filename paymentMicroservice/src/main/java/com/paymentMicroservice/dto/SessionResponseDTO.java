package com.paymentMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponseDTO {
    private String sessionId;
    private String sessionUrl;
    private UUID correlationId;
}
