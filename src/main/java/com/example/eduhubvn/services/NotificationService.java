package com.example.eduhubvn.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.eduhubvn.dtos.noti.NotificationDTO;
import com.example.eduhubvn.dtos.noti.NotificationReq;
import com.example.eduhubvn.entities.Notification;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.NotificationMapper;
import com.example.eduhubvn.repositories.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public NotificationDTO createNotification(NotificationReq req, User user) {
        Notification newNotification = new Notification();
        newNotification.setTitle(req.getTitle());
        newNotification.setMessage(req.getMessage());
        newNotification.setRead(req.isRead());
        newNotification.setUser(user);
        notificationRepository.save(newNotification);
        return notificationMapper.toDTO(newNotification);
    }

    public void markAsRead(Notification notification) {
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public List<NotificationDTO> getNotificationsForUser(User user) {
        List<Notification> notifications = notificationRepository.findAll().stream()
                .filter(noti -> noti.getUser().getId().equals(user.getId()))
                .toList();
        return notifications.stream()
                .map(notificationMapper::toDTO)
                .toList();
    }


}
