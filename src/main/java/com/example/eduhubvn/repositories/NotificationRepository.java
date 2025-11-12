package com.example.eduhubvn.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eduhubvn.entities.Notification;

public interface NotificationRepository  extends JpaRepository<Notification, UUID> {
    boolean existsByUserIdAndReadFalse(UUID userId);
    List<Notification> findTop5ByUserIdOrderByCreatedAtDesc(UUID userId);

}
