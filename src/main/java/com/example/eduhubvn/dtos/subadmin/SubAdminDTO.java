package com.example.eduhubvn.dtos.subadmin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.eduhubvn.enums.Permission;

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
