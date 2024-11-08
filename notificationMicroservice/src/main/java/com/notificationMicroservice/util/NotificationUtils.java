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

    public static void prepareNotification(Notification notification, NotificationRequestDTO notificationRequestDTO) {
        notification.setType(EMAIL);
        notification.setSender(senderEmail);
        notification.setRecipient(notificationRequestDTO.getRecipient());
        notification.setSubject("Movements Report");
        notification.setBody("Please find attached the movements report for the specified period.");
    }
}
