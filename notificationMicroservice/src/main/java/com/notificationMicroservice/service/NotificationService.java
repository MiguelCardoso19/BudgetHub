package com.notificationMicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.notificationMicroservice.dto.NotificationRequestDTO;
import com.notificationMicroservice.exception.FailedToSendEmailException;

public interface NotificationService {
    void handleEmailNotificationRequest(NotificationRequestDTO notificationRequestDTO) throws JsonProcessingException, FailedToSendEmailException;
    void retryFailedEmails() throws JsonProcessingException, FailedToSendEmailException;
}
