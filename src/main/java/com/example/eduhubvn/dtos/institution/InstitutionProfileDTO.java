package com.example.eduhubvn.dtos.institution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionProfileDTO {
    private InstitutionInfoDTO institution;
    private InstitutionUpdateDTO institutionUpdate;
}
