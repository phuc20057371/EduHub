package com.example.eduhubvn.dtos.subadmin.request;

import com.example.eduhubvn.entities.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignPermissionsRequest {
    private UUID subAdminId;
    private List<Permission> permissions;
}
