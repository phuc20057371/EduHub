package com.example.eduhubvn.dtos.noti;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReq {
    private String title;
    private String message;
    private boolean read;
}
