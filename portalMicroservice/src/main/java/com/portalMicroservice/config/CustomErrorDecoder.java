package com.portalMicroservice.config;

import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.authentication.UserNotFoundException;
import com.portalMicroservice.exception.authentication.EmailNotFoundException;
import com.portalMicroservice.exception.authentication.InvalidPasswordException;
import com.portalMicroservice.exception.authentication.InvalidRefreshTokenException;
import com.portalMicroservice.exception.authentication.UserCredentialsValidationException;
import com.portalMicroservice.exception.budget.*;
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
            case 409 -> handleConflict(methodKey, response, request);
        }

        return new GenericException();
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private void handleNotFound(String methodKey, HttpServletRequest request) throws UserNotFoundException, EmailNotFoundException, BudgetSubtypeNotFoundException, InvoiceNotFoundException, BudgetTypeNotFoundException, SupplierNotFoundException, MovementsNotFoundForBudgetTypeException, MovementsNotFoundForBudgetSubtypeException, MovementNotFoundException {
        if (methodKey.contains("refreshToken") || methodKey.contains("delete") || methodKey.contains("update")) {
            throw new UserNotFoundException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("signIn")) {
            throw new EmailNotFoundException((String) request.getAttribute("email"));
        } else if (methodKey.contains("updateSubtype") || methodKey.contains("deleteSubtype") || methodKey.contains("findSubtypeById")) {
            throw new BudgetSubtypeNotFoundException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("getInvoiceById") || methodKey.contains("updateInvoice") || methodKey.contains("deleteInvoice")) {
            throw new InvoiceNotFoundException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("updateBudgeType") || methodKey.contains("deleteBudgeType") || methodKey.contains("findBudgeTypeById")) {
            throw new BudgetTypeNotFoundException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("getSupplierById") || methodKey.contains("deleteSupplier") || methodKey.contains("updateSupplier")) {
            throw new SupplierNotFoundException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("getMovementsByBudgetType")) {
            throw new MovementsNotFoundForBudgetTypeException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("getMovementsByBudgetSubtype")) {
            throw new MovementsNotFoundForBudgetSubtypeException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("deleteMovement") || methodKey.contains("updateMovement")) {
            throw new MovementNotFoundException((UUID) request.getAttribute("id"));
        }
    }

    private void handleUnauthorized(String methodKey) throws InvalidPasswordException, InvalidRefreshTokenException {
        if (methodKey.contains("signIn") || methodKey.contains("delete") || methodKey.contains("update")) {
            throw new InvalidPasswordException();
        } else if (methodKey.contains("refreshToken")) {
            throw new InvalidRefreshTokenException();
        }
    }

    private void handleConflict(String methodKey, Response response, HttpServletRequest request) throws UserCredentialsValidationException, BudgetSubtypeAlreadyExistsException, BudgetTypeAlreadyExistsException, InvoiceAlreadyExistsException, SupplierValidationException, MovementValidationException, GenerateExcelException {
        if (methodKey.contains("register") || methodKey.contains("update")) {
            throw new UserCredentialsValidationException(FeignClientUtils.extractErrorMessage(response));
        } else if (methodKey.contains("addSubtype")) {
            throw new BudgetSubtypeAlreadyExistsException((String) request.getAttribute("name"));
        } else if (methodKey.contains("createBudgetType")) {
            throw new BudgetTypeAlreadyExistsException((String) request.getAttribute("name"));
        } else if (methodKey.contains("createInvoice")) {
            throw new InvoiceAlreadyExistsException((String) request.getAttribute("name"));
        } else if (methodKey.contains("createSupplier") || methodKey.contains("updateSupplier")) {
            throw new SupplierValidationException(FeignClientUtils.extractErrorMessage(response));
        } else if (methodKey.contains("createMovement") || methodKey.contains("updateMovement")) {
            throw new MovementValidationException(FeignClientUtils.extractErrorMessage(response));
        } else if (methodKey.contains("exportMovementsReport")) {
            throw new GenerateExcelException();
        }
    }
}