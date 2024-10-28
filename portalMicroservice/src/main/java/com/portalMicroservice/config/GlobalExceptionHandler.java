package com.portalMicroservice.config;

import com.portalMicroservice.exception.*;
import com.portalMicroservice.exception.authentication.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAuthorizationHeaderException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuthorizationHeaderException(InvalidAuthorizationHeaderException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException(EmailNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(),ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
    @ExceptionHandler(UserCredentialsValidationException.class)
    public ResponseEntity<ErrorResponse> handleUserCredentialsValidationException(UserCredentialsValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getStatus().value()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(GenericException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getStatus().value());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}