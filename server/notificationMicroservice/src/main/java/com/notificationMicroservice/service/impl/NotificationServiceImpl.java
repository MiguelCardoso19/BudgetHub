package com.notificationMicroservice.service.impl;

import com.notificationMicroservice.dto.NotificationRequestDTO;
import com.notificationMicroservice.exception.FailedToSendEmailException;
import com.notificationMicroservice.mapper.NotificationMapper;
import com.notificationMicroservice.model.Notification;
import com.notificationMicroservice.repository.NotificationRepository;
import com.notificationMicroservice.service.NotificationService;
import com.notificationMicroservice.service.S3Service;
import com.notificationMicroservice.util.NotificationUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import static com.notificationMicroservice.enumerators.NotificationStatus.*;
import static java.util.concurrent.TimeUnit.HOURS;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final S3Service s3Service;
    private final NotificationRepository notificationRepository;
    private final JavaMailSender emailSender;
    private final NotificationMapper notificationMapper;
    private final RedissonClient redissonClient;

    @Value("${email.retry.lock.key}")
    private String EMAIL_RETRY_LOCK_KEY;

    @Value("${EMAIL_RETRY_MAX_ATTEMPTS}")
    private int EMAIL_RETRY_MAX_ATTEMPTS;

    @Value("${mail.username}")
    private String SENDER_EMAIL;

    @Override
    @KafkaListener(topics = "notification-reset-password-topic", groupId = "notification_group", concurrency = "10", containerFactory = "notificationRequestKafkaListenerContainerFactory")
    public void handleEmailNotificationResetPassword(NotificationRequestDTO notificationRequestDTO) throws FailedToSendEmailException {
        handleEmailNotification(notificationRequestDTO, "reset-password");
    }

    @Override
    @KafkaListener(topics = "notification-stripe-receipt", groupId = "notification_group", concurrency = "10", containerFactory = "notificationRequestKafkaListenerContainerFactory")
    public void handleEmailNotificationStripeReceipt(NotificationRequestDTO notificationRequestDTO) throws FailedToSendEmailException {
        handleEmailNotification(notificationRequestDTO, "stripe-receipt");
    }

    @Override
    @KafkaListener(topics = "notification-topic", groupId = "notification_group", concurrency = "10", containerFactory = "notificationRequestKafkaListenerContainerFactory")
    public void handleEmailNotificationWithAttachmentRequest(NotificationRequestDTO notificationRequestDTO) throws FailedToSendEmailException {
        handleEmailNotification(notificationRequestDTO, "export-report");
    }

    @Scheduled(cron = "${RETRY_DELAY}")
    @Transactional
    @Override
    public void retryFailedEmails() {
        RLock lock = redissonClient.getLock(EMAIL_RETRY_LOCK_KEY);
        try {
            if (lock.tryLock(0, 1, HOURS)) {
                try {
                    for (Notification notification : notificationRepository.findByStatus(FAILED)) {
                        if (notification.getRetryCount() >= EMAIL_RETRY_MAX_ATTEMPTS) {
                            notification.setStatus(EXCEEDED_RETRIES);
                        } else {
                            handleNotificationRetry(notification);
                        }
                        notificationRepository.save(notification);
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleNotificationRetry(Notification notification) {
        try {
            if (notification.getFileKey() != null) {
                handleEmailNotificationWithAttachmentRequest(notificationMapper.toDTO(notification));
            } else if (notification.getBody().contains("password")) {
                handleEmailNotificationResetPassword(notificationMapper.toDTO(notification));
            } else {
                handleEmailNotificationStripeReceipt(notificationMapper.toDTO(notification));
            }

            notification.setStatus(SENT);
        } catch (FailedToSendEmailException e) {
            notification.setRetryCount(notification.getRetryCount() + 1);
        }
    }

    private void handleEmailNotification(NotificationRequestDTO notificationRequestDTO, String notificationType) throws FailedToSendEmailException {
        Notification notification = NotificationUtils.findOrInitializeNotification(notificationRequestDTO, notificationRepository, notificationMapper);
        try {

            if ("export-report".equals(notificationType) && notificationRequestDTO.getAttachment() != null) {
                String fileKey = s3Service.putObject(
                        Base64.getDecoder().decode(notificationRequestDTO.getAttachment()),
                        UUID.randomUUID() + ".xlsx"
                );
                notification.setFileKey(fileKey);
            }

            NotificationUtils.prepareNotification(notification, notificationRequestDTO, notificationType);
            sendEmail(notification);
            notification.setSender(SENDER_EMAIL);
            notification.setStatus(SENT);
        } catch (MessagingException | IOException e) {
            notification.setStatus(FAILED);
            throw new FailedToSendEmailException(notification.getRecipient());
        } finally {
            if (notification.getFileKey() == null) {
                notification.setBody(NotificationUtils.getDefaultBodyMessage(notificationType, notificationRequestDTO));
            }
            notificationRepository.save(notification);
        }
    }

    private void sendEmail(Notification notification) throws MessagingException, IOException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(notification.getRecipient());
        helper.setSubject(notification.getSubject());
        helper.setText(notification.getBody(), true);

        if (notification.getFileKey() != null) {
            byte[] fileBytes = s3Service.getObject(notification.getFileKey());
            helper.addAttachment("Movements_Report.xlsx", new ByteArrayResource(fileBytes));
        }

        emailSender.send(mimeMessage);
    }
}