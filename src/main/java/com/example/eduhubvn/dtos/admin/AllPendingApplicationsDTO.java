package com.example.eduhubvn.dtos.admin;

import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.PendingLecturerResponse;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllPendingApplicationsDTO {
    private List<PendingLecturerResponse> lecturers;
    private List<PendingEducationInstitutionDTO> educationInstitutions;
    private List<PendingPartnerOrganizationDTO> partnerOrganizations;
}
