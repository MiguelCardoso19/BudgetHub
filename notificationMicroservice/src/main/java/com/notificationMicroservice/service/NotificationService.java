package com.notificationMicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.notificationMicroservice.exception.FailedToSendEmailException;

public interface NotificationService {
    void sendEmail(String message) throws JsonProcessingException, FailedToSendEmailException;
    void retryFailedEmails() throws JsonProcessingException, FailedToSendEmailException;
}
