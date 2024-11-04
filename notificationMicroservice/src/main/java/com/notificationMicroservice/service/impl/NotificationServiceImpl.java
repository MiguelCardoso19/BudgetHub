package com.notificationMicroservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationMicroservice.exception.FailedToSendEmailException;
import com.notificationMicroservice.model.Notification;
import com.notificationMicroservice.repository.NotificationRepository;
import com.notificationMicroservice.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

import static com.notificationMicroservice.enumerators.NotificationStatus.*;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final JavaMailSender emailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    @Override
    @KafkaListener(topics = "email-notification-topic", groupId = "notification_group")
    public void sendEmail(String message) throws JsonProcessingException, FailedToSendEmailException {
        Notification notification = objectMapper.readValue(message, Notification.class);

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(notification.getFrom());
            helper.setTo(notification.getTo());
            helper.setSubject(notification.getSubject());
            helper.setText(notification.getBody());

            if (notification.getAttachment() != null) {
                byte[] attachmentBytes = Base64.getDecoder().decode(notification.getAttachment());
                helper.addAttachment("Movements_Report.xlsx", new ByteArrayDataSource(attachmentBytes, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            }

            emailSender.send(mimeMessage);
            notification.setStatus(SENT);
        } catch (Exception e) {
            notification.setStatus(FAILED);
            throw new FailedToSendEmailException(notification.getTo());
        } finally {
            notificationRepository.save(notification);
        }
    }

    @Scheduled(cron = "0 0 0,12 * * ?")
    @Transactional
    @Override
    public void retryFailedEmails() throws JsonProcessingException, FailedToSendEmailException {
        List<Notification> failedNotifications = notificationRepository.findByStatus(FAILED);

        for (Notification notification : failedNotifications) {
            sendEmail(objectMapper.writeValueAsString(notification));
        }
    }
}