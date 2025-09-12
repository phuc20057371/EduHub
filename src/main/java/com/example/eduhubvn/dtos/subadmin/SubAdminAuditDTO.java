package com.example.eduhubvn.dtos.subadmin;

import com.example.eduhubvn.entities.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubAdminAuditDTO {
    private UUID id;
    private UUID subAdminId;
    private String subAdminEmail;
    private Permission permission;
    private UUID assignedById;
    private String assignedByEmail;
    private LocalDateTime createdAt;
}
