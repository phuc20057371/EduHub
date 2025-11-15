package com.example.eduhubvn.dtos.institution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionPendingDTO {
    private InstitutionDTO educationInstitution;
    private InstitutionUpdateDTO educationInstitutionUpdate;
}
