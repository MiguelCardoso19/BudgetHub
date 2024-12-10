package com.notificationMicroservice.repository;

import com.notificationMicroservice.enumerators.NotificationStatus;
import com.notificationMicroservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByStatus(NotificationStatus notificationStatus);
}