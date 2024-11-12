package com.notificationMicroservice.util;

import com.notificationMicroservice.dto.NotificationRequestDTO;
import com.notificationMicroservice.mapper.NotificationMapper;
import com.notificationMicroservice.model.Notification;
import com.notificationMicroservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Value;

import static com.notificationMicroservice.enumerators.NotificationType.EMAIL;

public class NotificationUtils {

    @Value("${spring.mail.username}")
    private static String senderEmail;

    public static Notification findOrInitializeNotification(NotificationRequestDTO notificationRequestDTO, NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        return notificationRequestDTO.getId() != null ?
                notificationRepository.findById(notificationRequestDTO.getId()).orElse(new Notification()) :
                notificationMapper.toEntity(notificationRequestDTO);
    }

    public static void prepareExportReportNotification(Notification notification, NotificationRequestDTO notificationRequestDTO) {
        notification.setType(EMAIL);
        notification.setSender(senderEmail);
        notification.setRecipient(notificationRequestDTO.getRecipient());
        notification.setSubject("Movements Report");
        notification.setBody("Please find attached the movements report for the specified period.");
    }

    public static void prepareResetPasswordNotification(Notification notification, NotificationRequestDTO notificationRequestDTO) {
        notification.setType(EMAIL);
        notification.setSender(senderEmail);
        notification.setRecipient(notificationRequestDTO.getRecipient());
        notification.setSubject("Password Reset Request");

        String htmlContent = "<html>"
                + "<body style='font-family: Arial, sans-serif;'>"
                + "<p>We received a request to reset your password. Please click the link below to reset your password:</p>"
                + "<p><a href='" + notificationRequestDTO.getResetLink() + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Reset Password</a></p>"
                + "</body>"
                + "</html>";

        notification.setBody(htmlContent);
    }
}
