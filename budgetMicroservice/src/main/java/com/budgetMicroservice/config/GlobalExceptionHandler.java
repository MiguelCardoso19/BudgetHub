package com.budgetMicroservice.config;

import com.budgetMicroservice.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static com.budgetMicroservice.exception.ErrorMessage.MAX_UPLOAD_SIZE_EXCEEDED;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SupplierValidationException.class)
    public ResponseEntity<ErrorResponse> handleUserCredentialsValidationException(SupplierValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrors(),
                ex.getStatus().value(),
                ex.getErrorCode()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MovementValidationException.class)
    public ResponseEntity<ErrorResponse> handleMovementValidationException(MovementValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrors(),
                ex.getStatus().value(),
                ex.getErrorCode()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(BudgetTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBudgetTypeNotFoundException(BudgetTypeNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInvoiceNotFoundException(InvoiceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(BudgetSubtypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBudgetSubtypeNotFoundException(BudgetSubtypeNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(BudgetSubtypeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBudgetSubtypeAlreadyExitsException(BudgetSubtypeAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(BudgetTypeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBudgetTypeAlreadyExitsException(BudgetTypeAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSupplierNotFoundException(SupplierNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MovementNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovementNotFoundException(MovementNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MovementAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleMovementAlreadyExistsException(MovementAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MovementsNotFoundForBudgetTypeException.class)
    public ResponseEntity<ErrorResponse> handleMovementsNotFoundForBudgetTypeException(MovementsNotFoundForBudgetTypeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MovementsNotFoundForBudgetSubtypeException.class)
    public ResponseEntity<ErrorResponse> handleMovementsNotFoundForBudgetSubtypeException(MovementsNotFoundForBudgetSubtypeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(FailedToUploadFileException.class)
    public ResponseEntity<ErrorResponse> handleFailedToUploadFileException(FailedToUploadFileException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(InvoiceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleInvoiceAlreadyExistsException(InvoiceAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(MAX_UPLOAD_SIZE_EXCEEDED.getMessage(),
                                                        MAX_UPLOAD_SIZE_EXCEEDED.getStatus().value(),
                                                        MAX_UPLOAD_SIZE_EXCEEDED.getErrorCode());
        return new ResponseEntity<>(errorResponse, MAX_UPLOAD_SIZE_EXCEEDED.getStatus());    }
}