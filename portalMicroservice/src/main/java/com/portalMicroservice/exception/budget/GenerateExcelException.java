package com.portalMicroservice.exception.budget;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.portalMicroservice.exception.ErrorMessage.FAILED_TO_GENERATE_EXCEL;

@Getter
public class GenerateExcelException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public GenerateExcelException() {
        super(FAILED_TO_GENERATE_EXCEL.getMessage());
        this.message = FAILED_TO_GENERATE_EXCEL.getMessage();
        this.status = FAILED_TO_GENERATE_EXCEL.getStatus();
        this.errorCode = FAILED_TO_GENERATE_EXCEL.getErrorCode();
    }
}