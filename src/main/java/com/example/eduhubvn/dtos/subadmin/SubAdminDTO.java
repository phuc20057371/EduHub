package com.example.eduhubvn.dtos.subadmin;

import com.example.eduhubvn.entities.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubAdminDTO {
    private UUID id;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private List<Permission> permissions;
}
