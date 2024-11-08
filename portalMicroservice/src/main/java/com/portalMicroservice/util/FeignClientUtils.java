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

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.function.Consumer;

@Slf4j
public class FeignClientUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static void extractDetails(String body, ServletRequestAttributes attributes) {
        HttpServletRequest request = attributes.getRequest();

        processDTO(body, UserCredentialsDTO.class, user -> {
            request.setAttribute("nif", user.getNif());
            if (user.getId() != null) request.setAttribute("id", user.getId());
            if (user.getEmail() != null) request.setAttribute("email", user.getEmail());
        });

        processDTO(body, BudgetSubtypeDTO.class, budget -> {
            request.setAttribute("name", budget.getName());
            request.setAttribute("id", budget.getId());
        });

        processDTO(body, BudgetTypeDTO.class, budgetType -> {
            request.setAttribute("name", budgetType.getName());
            request.setAttribute("id", budgetType.getId());
        });

        processDTO(body, SupplierDTO.class, supplier ->
                request.setAttribute("id", supplier.getId())
        );

        processDTO(body, InvoiceDTO.class, invoice ->
                request.setAttribute("id", invoice.getId())
        );

        processDTO(body, MovementDTO.class, movement -> {
            if (movement.getId() != null) request.setAttribute("id", movement.getId());
            if (movement.getInvoiceId() != null) request.setAttribute("invoice-id", movement.getInvoiceId());
            if (movement.getBudgetSubtypeId() != null) request.setAttribute("budget-subtype-id", movement.getBudgetSubtypeId());
            if (movement.getBudgetTypeId() != null) request.setAttribute("budget-type-id", movement.getBudgetTypeId());
            if (movement.getSupplierId() != null) request.setAttribute("supplier-id", movement.getSupplierId());
        });
    }

    private static <T> void processDTO(String body, Class<T> dtoClass, Consumer<T> action) {
        try {
            T dto = objectMapper.readValue(body, dtoClass);
            if (dto != null) {
                action.accept(dto);
            }
        } catch (JsonProcessingException e) {
            log.info("Failed to process {}: {}", dtoClass.getSimpleName(), e.getMessage());
        }
    }


    @SneakyThrows
    public static String extractErrorMessage(Response response) {
        JsonNode bodyJson = objectMapper.readTree(response.body().asInputStream());
        String errorMessage = bodyJson.get("message").asText();

        return errorMessage.substring(errorMessage.indexOf("[") + 1, errorMessage.indexOf("]"));
    }
}