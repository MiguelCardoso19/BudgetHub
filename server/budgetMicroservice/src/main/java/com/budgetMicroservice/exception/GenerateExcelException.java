package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.budgetMicroservice.exception.ErrorMessage.FAILED_TO_GENERATE_EXCEL;

@Getter
public class GenerateExcelException extends Exception {
    private UUID correlationId;
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public GenerateExcelException() {
        super(FAILED_TO_GENERATE_EXCEL.getMessage());
        this.message = FAILED_TO_GENERATE_EXCEL.getMessage();
        this.status = FAILED_TO_GENERATE_EXCEL.getStatus();
        this.errorCode = FAILED_TO_GENERATE_EXCEL.getErrorCode();
    }

    public GenerateExcelException(UUID correlationId) {
        super(FAILED_TO_GENERATE_EXCEL.getMessage());
        this.correlationId = correlationId;
        this.message = FAILED_TO_GENERATE_EXCEL.getMessage();
        this.status = FAILED_TO_GENERATE_EXCEL.getStatus();
        this.errorCode = FAILED_TO_GENERATE_EXCEL.getErrorCode();
    }
}