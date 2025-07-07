package com.example.eduhubvn.dtos;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllPendingEntityDTO {
    private List<LecturerDTO> lecturers;
    private List<PartnerOrganizationDTO> partnerOrganizations;
    private List<EducationInstitutionDTO> educationInstitutions;
}
