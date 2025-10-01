package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.subadmin.SubAdminDTO;
import com.example.eduhubvn.dtos.subadmin.SubAdminAuditDTO;
import com.example.eduhubvn.dtos.subadmin.request.AssignPermissionsRequest;
import com.example.eduhubvn.dtos.subadmin.request.CreateSubAdminRequest;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.repositories.SubAdminPermissionRepository;
import com.example.eduhubvn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubAdminService {
    
    private final UserRepository userRepository;
    private final SubAdminPermissionRepository subAdminPermissionRepository;
    private final PasswordEncoder passwordEncoder;

    // Danh sách email phụ mẫu để thêm vào user khi tạo mới
    private static final List<String> SAMPLE_SUB_EMAILS = Arrays.asList(
            "work@example.com",
            "personal@gmail.com",
            "backup@outlook.com",
            "secondary@company.com"
    );

    private List<String> generateSampleSubEmails() {
        List<String> subEmails = new ArrayList<>();
        // Randomly select 0-2 sub emails from the sample list
        int numberOfSubEmails = (int) (Math.random() * 3); // 0, 1, or 2

        for (int i = 0; i < numberOfSubEmails; i++) {
            String subEmail = SAMPLE_SUB_EMAILS.get(i);
            // Append a random number to make it unique for each user
            String uniqueSubEmail = subEmail.replace("@", "+" + System.currentTimeMillis() + "@");
            subEmails.add(uniqueSubEmail);
        }

        return subEmails;
    }

    @Transactional
    public SubAdminDTO createSubAdmin(CreateSubAdminRequest request, User adminUser) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        
        // Create new sub admin user
        User subAdmin = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.SUB_ADMIN)
                .build();
        
        User savedSubAdmin = userRepository.save(subAdmin);
        
        return convertToDTO(savedSubAdmin);
    }
    
    @Transactional
    public SubAdminDTO assignPermissions(AssignPermissionsRequest request, User adminUser) {
        User subAdmin = userRepository.findById(request.getSubAdminId())
                .orElseThrow(() -> new RuntimeException("Sub admin not found"));
        
        if (subAdmin.getRole() != Role.SUB_ADMIN) {
            throw new RuntimeException("User is not a sub admin");
        }
        
        // Remove all existing permissions for this sub admin
        subAdminPermissionRepository.deleteByUserId(request.getSubAdminId());
        
        // Add new permissions
        for (Permission permission : request.getPermissions()) {
            SubAdminPermission subAdminPermission = SubAdminPermission.builder()
                    .user(subAdmin)
                    .permission(permission)
                    .assignedBy(adminUser)
                    .build();
            subAdminPermissionRepository.save(subAdminPermission);
        }
        
        return getSubAdminById(request.getSubAdminId());
    }
    
    public List<SubAdminDTO> getAllSubAdmins() {
        List<User> subAdmins = userRepository.findByRole(Role.SUB_ADMIN);
        return subAdmins.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public SubAdminDTO getSubAdminById(UUID subAdminId) {
        User subAdmin = userRepository.findById(subAdminId)
                .orElseThrow(() -> new RuntimeException("Sub admin not found"));
        
        if (subAdmin.getRole() != Role.SUB_ADMIN) {
            throw new RuntimeException("User is not a sub admin");
        }
        
        return convertToDTO(subAdmin);
    }
    
    @Transactional
    public void deleteSubAdmin(UUID subAdminId) {
        User subAdmin = userRepository.findById(subAdminId)
                .orElseThrow(() -> new RuntimeException("Sub admin not found"));
        
        if (subAdmin.getRole() != Role.SUB_ADMIN) {
            throw new RuntimeException("User is not a sub admin");
        }
        
        // Delete all permissions first
        subAdminPermissionRepository.deleteByUserId(subAdminId);
        
        // Delete the user
        userRepository.delete(subAdmin);
    }
    
    public List<Permission> getAvailablePermissions() {
        return List.of(Permission.values());
    }
    
    public List<String> getUserPermissions(UUID userId) {
        List<Permission> permissions = subAdminPermissionRepository.findPermissionsByUserId(userId);
        return permissions.stream()
                .map(Permission::name)
                .collect(Collectors.toList());
    }
    
    public List<SubAdminAuditDTO> getSubAdminAuditTrail(UUID subAdminId) {
        User subAdmin = userRepository.findById(subAdminId)
                .orElseThrow(() -> new RuntimeException("Sub admin not found"));
        
        if (subAdmin.getRole() != Role.SUB_ADMIN) {
            throw new RuntimeException("User is not a sub admin");
        }
        
        List<SubAdminPermission> permissions = subAdminPermissionRepository.findByUserId(subAdminId);
        
        return permissions.stream()
                .map(this::convertToAuditDTO)
                .collect(Collectors.toList());
    }
    
    private SubAdminAuditDTO convertToAuditDTO(SubAdminPermission permission) {
        return SubAdminAuditDTO.builder()
                .id(permission.getId())
                .subAdminId(permission.getUser().getId())
                .subAdminEmail(permission.getUser().getEmail())
                .permission(permission.getPermission())
                .assignedById(permission.getAssignedBy().getId())
                .assignedByEmail(permission.getAssignedBy().getEmail())
                .createdAt(permission.getCreatedAt())
                .build();
    }
    
    @Transactional
    public void assignPermissionsToUser(User subAdmin, List<Permission> permissions, User adminUser) {
        // Re-fetch the users to ensure they are managed entities
        User managedSubAdmin = userRepository.findById(subAdmin.getId())
                .orElseThrow(() -> new RuntimeException("Sub admin not found"));
        User managedAdminUser = userRepository.findById(adminUser.getId())
                .orElseThrow(() -> new RuntimeException("Admin user not found"));
        
        if (managedSubAdmin.getRole() != Role.SUB_ADMIN) {
            throw new RuntimeException("User is not a sub admin");
        }
        
        // Remove all existing permissions for this sub admin
        subAdminPermissionRepository.deleteByUserId(managedSubAdmin.getId());
        
        // Add new permissions
        for (Permission permission : permissions) {
            SubAdminPermission subAdminPermission = SubAdminPermission.builder()
                    .user(managedSubAdmin)
                    .permission(permission)
                    .assignedBy(managedAdminUser)
                    .build();
            subAdminPermissionRepository.save(subAdminPermission);
        }
    }
    
    @Transactional
    public SubAdminDTO resetPassword(UUID subAdminId, String newPassword, User adminUser) {
        User subAdmin = userRepository.findById(subAdminId)
                .orElseThrow(() -> new RuntimeException("Sub admin not found"));
        
        if (subAdmin.getRole() != Role.SUB_ADMIN) {
            throw new RuntimeException("User is not a sub admin");
        }
        
        // Encode and set new password
        subAdmin.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(subAdmin);
        
        return convertToDTO(subAdmin);
    }
    
    private SubAdminDTO convertToDTO(User user) {
        List<Permission> permissions = subAdminPermissionRepository.findPermissionsByUserId(user.getId());
        
        return SubAdminDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .permissions(permissions)
                .build();
    }
}
