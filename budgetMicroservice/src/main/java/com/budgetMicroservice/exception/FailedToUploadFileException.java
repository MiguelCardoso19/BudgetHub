package com.budgetMicroservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.budgetMicroservice.exception.ErrorMessage.FAILED_TO_UPLOAD_FILE;

@Getter
public class FailedToUploadFileException extends Exception {
    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    public FailedToUploadFileException(UUID id) {
        super(FAILED_TO_UPLOAD_FILE.getMessage(id));
        this.message = FAILED_TO_UPLOAD_FILE.getMessage(id);
        this.status = FAILED_TO_UPLOAD_FILE.getStatus();
        this.errorCode = FAILED_TO_UPLOAD_FILE.getErrorCode();
    }
}
