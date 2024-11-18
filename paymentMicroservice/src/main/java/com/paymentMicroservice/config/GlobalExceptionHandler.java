package com.paymentMicroservice.config;

import com.paymentMicroservice.exception.BudgetExceededException;
import com.paymentMicroservice.exception.ErrorResponse;
import com.paymentMicroservice.exception.FailedToCancelPaymentException;
import com.paymentMicroservice.exception.FailedToConfirmPaymentException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(FailedToConfirmPaymentException.class)
    public ResponseEntity<ErrorResponse> handleFailedToConfirmPaymentException(FailedToConfirmPaymentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(FailedToCancelPaymentException.class)
    public ResponseEntity<ErrorResponse> handleFailedToCancelPaymentException(FailedToCancelPaymentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(BudgetExceededException.class)
    public ResponseEntity<ErrorResponse> handleBudgetExceededException(BudgetExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}