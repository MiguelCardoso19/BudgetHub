package com.notificationMicroservice.config;

import com.notificationMicroservice.exception.ErrorResponse;
import com.notificationMicroservice.exception.FailedToSendEmailException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FailedToSendEmailException.class)
    public ResponseEntity<ErrorResponse> handleFailedToSendEmailException(FailedToSendEmailException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}