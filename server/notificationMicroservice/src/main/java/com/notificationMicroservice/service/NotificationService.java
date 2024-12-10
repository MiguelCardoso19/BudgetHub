package com.notificationMicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.notificationMicroservice.dto.NotificationRequestDTO;
import com.notificationMicroservice.exception.FailedToSendEmailException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface NotificationService {
    void handleEmailNotificationWithAttachmentRequest(NotificationRequestDTO notificationRequestDTO) throws IOException, FailedToSendEmailException, MessagingException;
    void retryFailedEmails() throws JsonProcessingException, FailedToSendEmailException;
    void handleEmailNotificationResetPassword(NotificationRequestDTO notificationRequestDTO) throws FailedToSendEmailException, MessagingException, IOException;
    void handleEmailNotificationStripeReceipt(NotificationRequestDTO notificationRequestDTO) throws FailedToSendEmailException, MessagingException, IOException;
}