package com.portalMicroservice.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.portalMicroservice.exception.ErrorMessage.ACCESS_DENIED;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(ACCESS_DENIED.getStatus().value());
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"errorCode\": \"" + ACCESS_DENIED.getErrorCode() +
                "\", \"message\": \"" + ACCESS_DENIED.getMessage() +
                "\", \"status\": \"" + ACCESS_DENIED.getStatus().value() +
                "\"}"
        );
    }
}
