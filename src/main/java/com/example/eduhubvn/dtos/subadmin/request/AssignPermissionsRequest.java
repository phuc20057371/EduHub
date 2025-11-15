package com.example.eduhubvn.dtos.subadmin.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.example.eduhubvn.enums.Permission;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignPermissionsRequest {
    private UUID subAdminId;
    private List<Permission> permissions;
}
