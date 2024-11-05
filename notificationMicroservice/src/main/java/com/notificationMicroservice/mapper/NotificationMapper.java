package com.notificationMicroservice.mapper;

import com.notificationMicroservice.dto.NotificationRequestDTO;
import com.notificationMicroservice.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "sentDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "type", ignore = true)
    Notification toEntity(NotificationRequestDTO notificationRequest);
}
