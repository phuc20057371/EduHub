package com.example.eduhubvn.dtos;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
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
public class UserProfileDTO {
    private UUID id;
    private String email;
    private String role;
    private LocalDateTime lastLogin;

    private LecturerDTO lecturer;
    private EducationInstitutionDTO educationInstitution;
    private PartnerOrganizationDTO partnerOrganization;

}
