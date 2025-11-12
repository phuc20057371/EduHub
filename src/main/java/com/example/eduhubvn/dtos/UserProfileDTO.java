package com.example.eduhubvn.dtos;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.noti.NotificationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
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
public class UserProfileDTO {
    private UUID id;
    private String email;
    private String role;
    private LocalDateTime lastLogin;
    private List<String> subEmails;

    private LecturerDTO lecturer;
    private EducationInstitutionDTO educationInstitution;
    private PartnerOrganizationDTO partnerOrganization;
    
    // Permissions for SUB_ADMIN role
    private List<String> permissions;

    private List<NotificationDTO> notifications;

}
