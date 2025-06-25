package com.example.eduhubvn.dtos;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.PendingLecturerDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Integer id;
    private String email;
    private String phone;
    private String role;

    private LecturerDTO lecturer;
    private PendingLecturerDTO pendingLecturer;

    private EducationInstitutionDTO educationInstitution;
    private PendingEducationInstitutionDTO pendingEducationInstitution;

    private PendingPartnerOrganizationDTO pendingPartnerOrganization;
    private PartnerOrganizationDTO partnerOrganization;



}
