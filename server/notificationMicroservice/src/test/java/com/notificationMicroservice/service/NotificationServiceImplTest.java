package com.notificationMicroservice.service;

import com.notificationMicroservice.dto.NotificationRequestDTO;
import com.notificationMicroservice.exception.FailedToSendEmailException;
import com.notificationMicroservice.mapper.NotificationMapper;
import com.notificationMicroservice.model.Notification;
import com.notificationMicroservice.repository.NotificationRepository;
import com.notificationMicroservice.service.impl.NotificationServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.notificationMicroservice.enumerators.NotificationStatus.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class NotificationServiceImplTest {

    @Value("${RETRY_DELAY}")
    private String EMAIL_RETRY_LOCK_KEY;

    @Mock
    private S3Service s3Service;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock lock;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private NotificationRequestDTO notificationRequestDTO;

    @BeforeEach
    public void setUp() throws InterruptedException {
        openMocks(this);

        when(redissonClient.getLock(EMAIL_RETRY_LOCK_KEY)).thenReturn(lock);
        when(lock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        notificationRequestDTO = createNotificationRequestDTO();

        Notification failedNotification = new Notification();
        failedNotification.setStatus(FAILED);
        failedNotification.setRetryCount(2);
        when(notificationRepository.findByStatus(FAILED)).thenReturn(List.of(failedNotification));
    }

    @Test
    public void testHandleEmailNotificationResetPassword() throws FailedToSendEmailException, MessagingException {
        Notification notification = new Notification();
        when(notificationMapper.toEntity(notificationRequestDTO)).thenReturn(notification);
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(s3Service.putObject(any(byte[].class), anyString())).thenReturn(UUID.randomUUID().toString());

        MimeMessage mimeMessage = prepareMimeMessage();
        doNothing().when(emailSender).send(mimeMessage);

        notificationService.handleEmailNotificationResetPassword(notificationRequestDTO);

        verify(notificationRepository).save(notification);
        assertEquals(SENT, notification.getStatus());
    }

    @Test
    public void testHandleEmailNotificationStripeReceipt() throws FailedToSendEmailException, MessagingException, IOException {
        Notification notification = new Notification();
        when(notificationMapper.toEntity(notificationRequestDTO)).thenReturn(notification);
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(s3Service.putObject(any(byte[].class), anyString())).thenReturn(UUID.randomUUID().toString());

        MimeMessage mimeMessage = prepareMimeMessage();
        doNothing().when(emailSender).send(mimeMessage);

        notificationService.handleEmailNotificationStripeReceipt(notificationRequestDTO);

        verify(notificationRepository).save(notification);
        assertEquals(SENT, notification.getStatus());
    }

    @Test
    public void testHandleEmailNotificationWithAttachmentRequest() throws FailedToSendEmailException, MessagingException, IOException {
        notificationRequestDTO.setAttachment(Base64.getEncoder().encodeToString("some-attachment-in-base64".getBytes()));
        assertNotNull(Base64.getDecoder().decode(notificationRequestDTO.getAttachment()), "Attachment byte array should not be null");

        Notification notification = new Notification();
        when(notificationMapper.toEntity(notificationRequestDTO)).thenReturn(notification);
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(s3Service.getObject(anyString())).thenReturn("mock file content".getBytes());

        MimeMessage mimeMessage = prepareMimeMessage();
        doNothing().when(emailSender).send(mimeMessage);

        notificationService.handleEmailNotificationWithAttachmentRequest(notificationRequestDTO);

        verify(notificationRepository).save(notification);
        assertEquals(SENT, notification.getStatus());
    }

    @Test
    public void testRetryFailedEmails_MaxRetryExceeded() throws InterruptedException {
        Notification failedNotification = new Notification();
        failedNotification.setStatus(FAILED);
        failedNotification.setRetryCount(3);
        when(notificationRepository.findByStatus(FAILED)).thenReturn(List.of(failedNotification));

        notificationService.retryFailedEmails();

        verify(redissonClient).getLock(EMAIL_RETRY_LOCK_KEY);
        verify(lock).tryLock(anyLong(), anyLong(), any());
        verify(notificationRepository).save(failedNotification);
        assertEquals(EXCEEDED_RETRIES, failedNotification.getStatus());
    }

    private NotificationRequestDTO createNotificationRequestDTO() {
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setRecipient("test@domain.com");
        dto.setAttachment(Base64.getEncoder().encodeToString("attachment-content".getBytes()));
        dto.setResetLink("http://localhost:8080/api/v1/auth/reset-password?token=abc123xyz");
        dto.setStripeReceiptUrl("https://stripe.com/receipts/example123");
        return dto;
    }

    private MimeMessage prepareMimeMessage() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        return mimeMessage;
    }
}