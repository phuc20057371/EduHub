package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.subadmin.SubAdminDTO;
import com.example.eduhubvn.dtos.subadmin.SubAdminAuditDTO;
import com.example.eduhubvn.dtos.subadmin.request.AssignPermissionsRequest;
import com.example.eduhubvn.dtos.subadmin.request.CreateSubAdminRequest;
import com.example.eduhubvn.dtos.subadmin.request.ResetPasswordRequest;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.enums.Permission;
import com.example.eduhubvn.services.SubAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/sub-admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SubAdminController {
    
    private final SubAdminService subAdminService;
    
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SubAdminDTO>> createSubAdmin(
            @RequestBody CreateSubAdminRequest request,
            @AuthenticationPrincipal User adminUser) {
        try {
            SubAdminDTO subAdmin = subAdminService.createSubAdmin(request, adminUser);
            return ResponseEntity.ok(ApiResponse.<SubAdminDTO>builder()
                    .success(true)
                    .message("Sub admin created successfully")
                    .data(subAdmin)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<SubAdminDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    
    @PostMapping("/assign-permissions")
    public ResponseEntity<ApiResponse<SubAdminDTO>> assignPermissions(
            @RequestBody AssignPermissionsRequest request,
            @AuthenticationPrincipal User adminUser) {
        try {
            SubAdminDTO subAdmin = subAdminService.assignPermissions(request, adminUser);
            return ResponseEntity.ok(ApiResponse.<SubAdminDTO>builder()
                    .success(true)
                    .message("Permissions assigned successfully")
                    .data(subAdmin)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<SubAdminDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SubAdminDTO>>> getAllSubAdmins() {
        try {
            List<SubAdminDTO> subAdmins = subAdminService.getAllSubAdmins();
            return ResponseEntity.ok(ApiResponse.<List<SubAdminDTO>>builder()
                    .success(true)
                    .message("Sub admins retrieved successfully")
                    .data(subAdmins)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<SubAdminDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubAdminDTO>> getSubAdminById(@PathVariable UUID id) {
        try {
            SubAdminDTO subAdmin = subAdminService.getSubAdminById(id);
            return ResponseEntity.ok(ApiResponse.<SubAdminDTO>builder()
                    .success(true)
                    .message("Sub admin retrieved successfully")
                    .data(subAdmin)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<SubAdminDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubAdmin(@PathVariable UUID id) {
        try {
            subAdminService.deleteSubAdmin(id);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .success(true)
                    .message("Sub admin deleted successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/available-permissions")
    public ResponseEntity<ApiResponse<List<Permission>>> getAvailablePermissions() {
        try {
            List<Permission> permissions = subAdminService.getAvailablePermissions();
            return ResponseEntity.ok(ApiResponse.<List<Permission>>builder()
                    .success(true)
                    .message("Available permissions retrieved successfully")
                    .data(permissions)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<Permission>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/{id}/audit-trail")
    public ResponseEntity<ApiResponse<List<SubAdminAuditDTO>>> getSubAdminAuditTrail(@PathVariable UUID id) {
        try {
            List<SubAdminAuditDTO> auditTrail = subAdminService.getSubAdminAuditTrail(id);
            return ResponseEntity.ok(ApiResponse.<List<SubAdminAuditDTO>>builder()
                    .success(true)
                    .message("Audit trail retrieved successfully")
                    .data(auditTrail)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<List<SubAdminAuditDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<SubAdminDTO>> resetPassword(
            @RequestBody ResetPasswordRequest request,
            @AuthenticationPrincipal User adminUser) {
        try {
            SubAdminDTO subAdmin = subAdminService.resetPassword(
                    request.getSubAdminId(), 
                    request.getNewPassword(), 
                    adminUser
            );
            return ResponseEntity.ok(ApiResponse.<SubAdminDTO>builder()
                    .success(true)
                    .message("Password reset successfully")
                    .data(subAdmin)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<SubAdminDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }
}
