package com.example.eduhubvn.dtos.edu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EducationInstitutionPendingDTO {
    private EducationInstitutionDTO educationInstitution;
    private EducationInstitutionUpdateDTO educationInstitutionUpdate;
}
