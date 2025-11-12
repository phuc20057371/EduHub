package com.example.eduhubvn.dtos.noti;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private UUID id;
    private String title;
    private String message;
    private boolean read;

    private LocalDate createdAt;
    private LocalDate updatedAt;

}
