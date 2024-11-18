package com.paymentMicroservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentMicroservice.dto.MovementUpdateStatusRequestDTO;
import com.paymentMicroservice.enumerators.MovementStatus;

import java.util.Map;
import java.util.UUID;

public class WebhookUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, UUID> parseItemsMetadata(Map<String, String> metadata) throws JsonProcessingException {
        Map<String, Map<String, String>> items = objectMapper.readValue(metadata.get("items"), new TypeReference<>() {
        });

        UUID budgetTypeId = null;
        UUID budgetSubtypeId = null;
        UUID supplierId = null;

        for (Map<String, String> itemDetails : items.values()) {
            supplierId = UUID.fromString(itemDetails.get("supplierId"));

            if (itemDetails.containsKey("budgetTypeId") && budgetTypeId == null) {
                budgetTypeId = UUID.fromString(itemDetails.get("budgetTypeId"));
            }
            if (itemDetails.containsKey("budgetSubtypeId") && budgetSubtypeId == null) {
                budgetSubtypeId = UUID.fromString(itemDetails.get("budgetSubtypeId"));
            }
        }

        return Map.of("budgetTypeId", budgetTypeId, "budgetSubtypeId", budgetSubtypeId, "supplierId", supplierId);
    }

    public static MovementUpdateStatusRequestDTO buildMovementUpdateRequestDTO(MovementStatus status, String documentNumber) {
        MovementUpdateStatusRequestDTO movementUpdateStatusRequestDTO = new MovementUpdateStatusRequestDTO();
        movementUpdateStatusRequestDTO.setDocumentNumber(documentNumber);
        movementUpdateStatusRequestDTO.setStatus(status);
        return movementUpdateStatusRequestDTO;
    }
}