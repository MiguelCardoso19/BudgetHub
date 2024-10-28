package com.portalMicroservice.config;

import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.authentication.UserNotFoundException;
import com.portalMicroservice.exception.authentication.EmailNotFoundException;
import com.portalMicroservice.exception.authentication.InvalidPasswordException;
import com.portalMicroservice.exception.authentication.InvalidRefreshTokenException;
import com.portalMicroservice.exception.authentication.UserCredentialsValidationException;
import com.portalMicroservice.util.FeignClientUtils;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Component
public class CustomErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpServletRequest request = getCurrentHttpRequest();

        switch (response.status()) {
            case 404 -> handleNotFound(methodKey, request);
            case 401 -> handleUnauthorized(methodKey);
            case 409 -> handleConflict(methodKey, response);
        }

        return new GenericException();
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private void handleNotFound(String methodKey, HttpServletRequest request) throws UserNotFoundException, EmailNotFoundException {
        if (methodKey.contains("refreshToken") || methodKey.contains("delete") || methodKey.contains("update")) {
            throw new UserNotFoundException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("signIn")) {
            throw new EmailNotFoundException((String) request.getAttribute("email"));
        }
    }

    private void handleUnauthorized(String methodKey) throws InvalidPasswordException, InvalidRefreshTokenException {
        if (methodKey.contains("signIn") || methodKey.contains("delete") || methodKey.contains("update")) {
            throw new InvalidPasswordException();
        } else if (methodKey.contains("refreshToken")) {
            throw new InvalidRefreshTokenException();
        }
    }

    private void handleConflict(String methodKey, Response response) throws UserCredentialsValidationException {
        if (methodKey.contains("register") || methodKey.contains("update")) {
            throw new UserCredentialsValidationException(FeignClientUtils.extractErrorMessage(response));
        }
    }
}