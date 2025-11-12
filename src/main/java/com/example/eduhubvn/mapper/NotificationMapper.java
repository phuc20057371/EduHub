package com.example.eduhubvn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.eduhubvn.dtos.noti.NotificationDTO;
import com.example.eduhubvn.entities.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDTO toDTO(Notification notification);

    @Mapping(target = "user", ignore = true)
    Notification toEntity(NotificationDTO notificationDTO);

    @Mapping(target = "user", ignore = true)
    void updateNotificationFromDTO(NotificationDTO notificationDTO, @MappingTarget Notification notification);

    List<NotificationDTO> toDTOList(List<Notification> notifications);
}
