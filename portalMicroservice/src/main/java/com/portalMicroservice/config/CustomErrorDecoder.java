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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpServletRequest request = getCurrentHttpRequest();

        switch (response.status()) {
            case 404 -> handleNotFound(methodKey, request);
            case 401 -> handleUnauthorized(methodKey);
            case 409 -> handleConflict(methodKey, response, request);
            case 400 -> handleBadRequest(methodKey, response);
        }

        return new GenericException();
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private void handleNotFound(String methodKey, HttpServletRequest request) throws UserNotFoundException, EmailNotFoundException, BudgetSubtypeNotFoundException, InvoiceNotFoundException, BudgetTypeNotFoundException, SupplierNotFoundException, MovementsNotFoundForBudgetTypeException, MovementsNotFoundForBudgetSubtypeException, MovementNotFoundException {
        String path = request.getRequestURI();
        String[] pathParts = path.split("/");
        Optional<String> optionalId = Optional.ofNullable(pathParts[pathParts.length - 1]).filter(id -> !id.isEmpty());

        if (methodKey.contains("refreshToken") || methodKey.contains("delete") || methodKey.contains("update")) {
            throw new UserNotFoundException((UUID) request.getAttribute("id"));
        } else if (methodKey.contains("signIn")) {
            throw new EmailNotFoundException((String) request.getAttribute("email"));
        } else if (methodKey.contains("updateSubtype") || methodKey.contains("deleteSubtype") || methodKey.contains("findSubtypeById")) {
            throw new BudgetSubtypeNotFoundException(optionalId.orElseGet(() -> (String) request.getAttribute("id")));
        } else if (methodKey.contains("getInvoiceById") || methodKey.contains("updateInvoice") || methodKey.contains("deleteInvoice")) {
            throw new InvoiceNotFoundException(optionalId.orElseGet(() -> (String) request.getAttribute("id")));
        } else if (methodKey.contains("updateBudgeType") || methodKey.contains("deleteBudgeType") || methodKey.contains("findBudgeTypeById")) {
            throw new BudgetTypeNotFoundException(optionalId.orElseGet(() -> (String) request.getAttribute("id")));
        } else if (methodKey.contains("getSupplierById") || methodKey.contains("deleteSupplier") || methodKey.contains("updateSupplier")) {
            throw new SupplierNotFoundException(optionalId.orElseGet(() -> (String) request.getAttribute("id")));
        } else if (methodKey.contains("getMovementsByBudgetType")) {
            throw new MovementsNotFoundForBudgetTypeException(optionalId.orElseGet(() -> (String) request.getAttribute("id")));
        } else if (methodKey.contains("getMovementsByBudgetSubtype")) {
            throw new MovementsNotFoundForBudgetSubtypeException(optionalId.orElseGet(() -> (String) request.getAttribute("id")));
        } else if (methodKey.contains("deleteMovement") || methodKey.contains("updateMovement")) {
            throw new MovementNotFoundException(optionalId.orElseGet(() -> (String) request.getAttribute("id")));
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
            throw new UserCredentialsValidationException(FeignClientUtils.formatedExtractedErrorMessageList(response));
        } else if (methodKey.contains("addSubtype")) {
            throw new BudgetSubtypeAlreadyExistsException((String) request.getAttribute("name"));
        } else if (methodKey.contains("createBudgetType")) {
            throw new BudgetTypeAlreadyExistsException((String) request.getAttribute("name"));
        } else if (methodKey.contains("createInvoice")) {
            throw new InvoiceAlreadyExistsException((String) request.getAttribute("name"));
        } else if (methodKey.contains("createSupplier") || methodKey.contains("updateSupplier")) {
            throw new SupplierValidationException(FeignClientUtils.formatedExtractedErrorMessageList(response));
        } else if (methodKey.contains("createMovement") || methodKey.contains("updateMovement")) {
            throw new MovementValidationException(FeignClientUtils.formatedExtractedErrorMessageList(response));
        } else if (methodKey.contains("exportMovementsReport")) {
            throw new GenerateExcelException();
        }
    }

    private void handleBadRequest(String methodKey, Response response) throws BudgetExceededException {
        if (methodKey.contains("createMovement") || methodKey.contains("updateMovement")) {
            throw new BudgetExceededException(FeignClientUtils.extractErrorMessage(response));
        } else if(methodKey.contains("addSubtype") || methodKey.contains("updateBudgeType")) {
            throw new BudgetExceededException(FeignClientUtils.extractErrorMessage(response));
        }
    }
}