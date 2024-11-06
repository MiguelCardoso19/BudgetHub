package com.portalMicroservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portalMicroservice.dto.authentication.UserCredentialsDTO;
import com.portalMicroservice.dto.budget.*;
import feign.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

public class FeignClientUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static void extractDetails(String body, ServletRequestAttributes attributes) {

        HttpServletRequest request = attributes.getRequest();

        try {
            UserCredentialsDTO userCredentialsDTO = objectMapper.readValue(body, UserCredentialsDTO.class);
            if (userCredentialsDTO != null) {
                String userNif = userCredentialsDTO.getNif();
                UUID id = userCredentialsDTO.getId();
                String email = userCredentialsDTO.getEmail();

                request.setAttribute("nif", userNif);
                if (id != null) {
                    request.setAttribute("id", id);
                }
                if (email != null) {
                    request.setAttribute("email", email);
                }
                return;
            }
        } catch (JsonProcessingException e) {
        }

        try {
            BudgetSubtypeDTO budgetSubtypeDTO = objectMapper.readValue(body, BudgetSubtypeDTO.class);
            if (budgetSubtypeDTO != null) {
                request.setAttribute("name", budgetSubtypeDTO.getName());
                request.setAttribute("id", budgetSubtypeDTO.getId());
                return;
            }
        } catch (JsonProcessingException e) {
        }

        try {
            BudgetTypeDTO budgetTypeDTO = objectMapper.readValue(body, BudgetTypeDTO.class);
            if (budgetTypeDTO != null) {
                request.setAttribute("name", budgetTypeDTO.getName());
                request.setAttribute("id", budgetTypeDTO.getId());
                return;
            }
        } catch (JsonProcessingException e) {
        }

        try {
            SupplierDTO supplierDTO = objectMapper.readValue(body, SupplierDTO.class);
            if (supplierDTO != null) {
                request.setAttribute("id", supplierDTO.getId());
                return;
            }
        } catch (JsonProcessingException e) {
        }

        try {
            InvoiceDTO invoiceDTO = objectMapper.readValue(body, InvoiceDTO.class);
            if (invoiceDTO != null) {
                request.setAttribute("id", invoiceDTO.getId());
                return;
            }
        } catch (JsonProcessingException e) {
        }

        try {
            MovementDTO movementDTO = objectMapper.readValue(body, MovementDTO.class);
            if (movementDTO != null) {
                if (movementDTO.getId() != null) {
                    request.setAttribute("id", movementDTO.getId());
                }
                if (movementDTO.getInvoiceId() != null) {
                    request.setAttribute("invoice-id", movementDTO.getInvoiceId());
                }
                if (movementDTO.getBudgetSubtypeId() != null) {
                    request.setAttribute("budget-subtype-id", movementDTO.getBudgetSubtypeId());
                }
                if (movementDTO.getBudgetTypeId() != null) {
                    request.setAttribute("budget-type-id", movementDTO.getBudgetTypeId());
                }
                if (movementDTO.getSupplierId() != null) {
                    request.setAttribute("supplier-id", movementDTO.getSupplierId());
                }
            }
        } catch (JsonProcessingException e) {
        }
    }

    @SneakyThrows
    public static String extractErrorMessage(Response response) {
        JsonNode bodyJson = objectMapper.readTree(response.body().asInputStream());
        String errorMessage = bodyJson.get("message").asText();

        return errorMessage.substring(errorMessage.indexOf("[") + 1, errorMessage.indexOf("]"));
    }
}