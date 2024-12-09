package com.notificationMicroservice.util;

import com.notificationMicroservice.dto.NotificationRequestDTO;
import com.notificationMicroservice.mapper.NotificationMapper;
import com.notificationMicroservice.model.Notification;
import com.notificationMicroservice.repository.NotificationRepository;

import static com.notificationMicroservice.enumerators.NotificationType.EMAIL;

public class NotificationUtils {

    public static Notification findOrInitializeNotification(NotificationRequestDTO notificationRequestDTO, NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        return notificationRequestDTO.getId() != null ?
                notificationRepository.findById(notificationRequestDTO.getId()).orElse(new Notification()) :
                notificationMapper.toEntity(notificationRequestDTO);
    }

    public static String getDefaultBodyMessage(String notificationType, NotificationRequestDTO notificationRequestDTO) {
        return switch (notificationType) {
            case "reset-password" -> "We received a request to reset your password. Please check your email for further instructions.";
            case "stripe-receipt" -> "Your Stripe receipt has been generated: " + notificationRequestDTO.getStripeReceiptUrl();
            default -> "Notification was processed without additional details.";
        };
    }

    public static void prepareNotification(Notification notification, NotificationRequestDTO notificationRequestDTO, String notificationType) {
        switch (notificationType) {
            case "export-report" -> prepareExportReportNotification(notification, notificationRequestDTO);
            case "reset-password" -> prepareResetPasswordNotification(notification, notificationRequestDTO);
            case "stripe-receipt" -> prepareStripeReceiptNotification(notification, notificationRequestDTO);
        }
    }

    private static void prepareExportReportNotification(Notification notification, NotificationRequestDTO notificationRequestDTO) {
        notification.setType(EMAIL);
        notification.setRecipient(notificationRequestDTO.getRecipient());
        notification.setSubject("Movements Report");
        notification.setBody("Please find attached the movements report for the specified period.");
    }

    private static void prepareResetPasswordNotification(Notification notification, NotificationRequestDTO notificationRequestDTO) {
        notification.setType(EMAIL);
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

    private static void prepareStripeReceiptNotification(Notification notification, NotificationRequestDTO notificationRequestDTO) {
        notification.setType(EMAIL);
        notification.setRecipient(notificationRequestDTO.getRecipient());
        notification.setSubject("Your Receipt from Stripe");

        String htmlContent = "<html>"
                + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                + "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>"
                + "<h2 style='color: #4CAF50; text-align: center;'>Your Receipt</h2>"
                + "<p>Dear Customer,</p>"
                + "<p>We’ve processed your request successfully. You can view or download your receipt using the link below:</p>"
                + "<p style='text-align: center;'>"
                + "<a href='" + notificationRequestDTO.getStripeReceiptUrl() + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block;'>View Receipt</a>"
                + "</p>"
                + "<p>If you have any questions, feel free to contact us at support@example.com.</p>"
                + "<p style='font-size: 12px; color: #888;'>This email was sent from an unmonitored address. Please do not reply.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        notification.setBody(htmlContent);
    }
}
