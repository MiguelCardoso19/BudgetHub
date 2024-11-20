package com.notificationMicroservice.service.impl;

import com.notificationMicroservice.dto.NotificationRequestDTO;
import com.notificationMicroservice.exception.FailedToSendEmailException;
import com.notificationMicroservice.mapper.NotificationMapper;
import com.notificationMicroservice.model.Notification;
import com.notificationMicroservice.repository.NotificationRepository;
import com.notificationMicroservice.service.NotificationService;
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

import java.util.Base64;
import java.util.List;

import static com.notificationMicroservice.enumerators.NotificationStatus.FAILED;
import static com.notificationMicroservice.enumerators.NotificationStatus.SENT;
import static java.util.concurrent.TimeUnit.HOURS;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final JavaMailSender emailSender;
    private final NotificationMapper notificationMapper;
    private final RedissonClient redissonClient;

    @Value("${email.retry.lock.key}")
    private String EMAIL_RETRY_LOCK_KEY;

    @Override
    @KafkaListener(topics = "notification-topic", groupId = "notification_group", concurrency = "10", containerFactory = "notificationRequestKafkaListenerContainerFactory")
    public void handleEmailNotificationWithAttachmentRequest(NotificationRequestDTO notificationRequestDTO) throws FailedToSendEmailException {
        handleEmailNotification(notificationRequestDTO, "export-report");
    }

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

    @Scheduled(cron = "${RETRY_DELAY}")
    @Transactional
    @Override
    public void retryFailedEmails() {
        RLock lock = redissonClient.getLock(EMAIL_RETRY_LOCK_KEY);
        try {
            if (lock.tryLock(0, 1, HOURS)) {
                try {
                    List<Notification> failedNotifications = notificationRepository.findByStatus(FAILED);

                    for (Notification notification : failedNotifications) {
                        if (notification.getAttachment() != null) {
                            handleEmailNotificationWithAttachmentRequest(notificationMapper.toDTO(notification));
                        }
                        handleEmailNotificationResetPassword(notificationMapper.toDTO(notification));
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException | FailedToSendEmailException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleEmailNotification(NotificationRequestDTO notificationRequestDTO, String notificationType) throws FailedToSendEmailException {
        Notification notification = NotificationUtils.findOrInitializeNotification(notificationRequestDTO, notificationRepository, notificationMapper);

        try {
            NotificationUtils.prepareNotification(notification, notificationRequestDTO, notificationType);
            sendEmail(notification, notificationRequestDTO);
            notification.setStatus(SENT);
        } catch (MessagingException e) {
            notification.setStatus(FAILED);
            throw new FailedToSendEmailException(notification.getRecipient());
        } finally {
            if (notification.getAttachment() == null) {
                notification.setBody(NotificationUtils.getDefaultBodyMessage(notificationType, notificationRequestDTO));
            }
            notificationRepository.save(notification);
        }
    }

    private void sendEmail(Notification notification, NotificationRequestDTO notificationRequestDTO) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(notification.getRecipient());
        helper.setSubject(notification.getSubject());
        helper.setText(notification.getBody(), true);

        if (notification.getAttachment() != null) {
            byte[] attachmentBytes = Base64.getDecoder().decode(notificationRequestDTO.getAttachment());
            helper.addAttachment("Movements_Report.xlsx", new ByteArrayResource(attachmentBytes));
        }

        emailSender.send(mimeMessage);
    }
}