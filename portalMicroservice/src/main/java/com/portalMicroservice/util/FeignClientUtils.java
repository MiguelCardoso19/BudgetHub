package com.portalMicroservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portalMicroservice.dto.authentication.UserCredentialsDTO;
import feign.Response;
import lombok.SneakyThrows;

import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

public class FeignClientUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;

    public static void extractUserDetails(String body, ServletRequestAttributes attributes) throws JsonProcessingException {
        UserCredentialsDTO userCredentialsDTO = objectMapper.readValue(body, UserCredentialsDTO.class);
        if (userCredentialsDTO != null) {
            String userNif = userCredentialsDTO.getNif();
            UUID id = userCredentialsDTO.getId();
            String email = userCredentialsDTO.getEmail();

            attributes.getRequest().setAttribute("nif", userNif);
            if (id != null) {
                attributes.getRequest().setAttribute("id", id);
            }
            if (email != null) {
                attributes.getRequest().setAttribute("email", email);
            }
        }
    }

    @SneakyThrows
    public static String extractErrorMessage(Response response) {
        JsonNode bodyJson = objectMapper.readTree(response.body().asInputStream());
        String errorMessage = bodyJson.get("message").asText();

        return errorMessage.substring(errorMessage.indexOf("[") + 1, errorMessage.indexOf("]"));
    }
}