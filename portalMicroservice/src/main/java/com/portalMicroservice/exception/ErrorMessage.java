package com.portalMicroservice.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CONFLICT;

public enum ErrorMessage {
    INVALID_TOKEN("INVALID_TOKEN", "Invalid token or username mismatch", HttpStatus.UNAUTHORIZED),
    USER_ID_NOT_FOUND("USER_ID_NOT_FOUND", "User not found with ID: %s", HttpStatus.NOT_FOUND),
    USER_NAME_NOT_FOUND("USER_NAME_NOT_FOUND", "User not found with name: %s", HttpStatus.NOT_FOUND),
    INVALID_AUTHORIZATION_HEADER("INVALID_AUTHORIZATION_HEADER", "Authorization header missing or invalid", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("INVALID_PASSWORD", "This password is not valid", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("ROLE_ACCESS_DENIED", "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", "This refresh token is not valid", HttpStatus.UNAUTHORIZED),
    GENERIC_MESSAGE("GENERIC_MESSAGE", "An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_NOT_FOUND("EMAIL_NOT_FOUND","Email not found: %s", NOT_FOUND),
    USER_CREDENTIALS_VALIDATION_ERROR("USER_CREDENTIALS_VALIDATION_ERROR", "User operation failed due to the following error/s: %s", HttpStatus.CONFLICT),
    BUDGET_TYPE_NOT_FOUND("BUDGET_TYPE_NOT_FOUND", "Budget type not found with ID: %s", NOT_FOUND),
    JSON_PARSE_ERROR("JSON_PARSE_ERROR", "Invalid JSON format", BAD_REQUEST),
    MAX_UPLOAD_SIZE_EXCEEDED("MAX_UPLOAD_SIZE_EXCEEDED", "File size exceeds the maximum limit of 5MB", BAD_REQUEST),
    MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE("MOVEMENTS_NOT_FOUND_FOR_BUDGET_TYPE","No movements found for the budget type with ID : %s", NOT_FOUND),
    MOVEMENTS_NOT_FOUND_BETWEEN_DATES("MOVEMENTS_NOT_FOUND_BETWEEN_DATES","No movements found between the dates %s and %s", NOT_FOUND),
    MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE("MOVEMENTS_NOT_FOUND_FOR_BUDGET_SUBTYPE","No movements found for the budget subtype with ID : %s", NOT_FOUND),
    MOVEMENT_VALIDATION_ERROR("MOVEMENT_VALIDATION_ERROR", "Movement operation failed due to the following error/s: %s", CONFLICT),
    MOVEMENT_ALREADY_EXISTS("MOVEMENT_ALREADY_EXISTS", "A movement with the document number '%s' already exists", CONFLICT),
    FAILED_TO_UPLOAD_FILE("FAILED_TO_UPLOAD_FILE", "File upload failed for Invoice ID: %s", CONFLICT),
    INVOICE_ALREADY_EXISTS("INVOICE_ALREADY_EXISTS", "A invoice with the document number '%s' already exists", CONFLICT),
    MOVEMENT_NOT_FOUND("MOVEMENT_NOT_FOUND", "Movement not found with ID: %s", NOT_FOUND),
    INVOICE_NOT_FOUND("INVOICE_NOT_FOUND", "Invoice not found with ID: %s", NOT_FOUND),
    SUPPLIER_VALIDATION_ERROR("SUPPLIER_VALIDATION_ERROR", "Supplier operation failed due to the following error/s: %s", CONFLICT),
    SUPPLIER_NOT_FOUND("SUPPLIER_NOT_FOUND", "Supplier not found with ID: %s", NOT_FOUND),
    BUDGET_SUBTYPE_ALREADY_EXISTS("BUDGET_SUBTYPE_ALREADY_EXISTS", "A budget subtype with the name '%s' already exists", CONFLICT),
    BUDGET_TYPE_ALREADY_EXISTS("BUDGET_TYPE_ALREADY_EXISTS", "A budget type with the name '%s' already exists", CONFLICT),
    FAILED_TO_GENERATE_EXCEL("FAILED_TO_GENERATE_EXCEL", "Failed to generate the Excel file", CONFLICT),
    FAILED_TO_SEND_EMAIL("FAILED_TO_SEND_EMAIL", "Failed to send email to: %s", CONFLICT),
    BUDGET_SUBTYPE_NOT_FOUND("BUDGET_SUBTYPE_NOT_FOUND", "Budget subtype not found with ID: %s", NOT_FOUND),
    OPTIMISTIC_LOCKING_FAILURE("OPTIMISTIC_LOCKING_FAILURE", "This entity has been modified by another transaction", CONFLICT),
    BUDGET_EXCEEDED("BUDGET_EXCEEDED", "%s", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;

    ErrorMessage(String code, String message, HttpStatus status) {
        this.errorCode = code;
        this.message = message;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
