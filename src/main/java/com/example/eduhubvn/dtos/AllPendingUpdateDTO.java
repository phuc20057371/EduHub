package com.example.eduhubvn.dtos;

import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllPendingUpdateDTO {
    private List<LecturerPendingDTO> lecturerUpdates;
    private List<PartnerOrganizationPendingDTO> partnerUpdates;
    private List<EducationInstitutionPendingDTO> educationInstitutionUpdates;
}
