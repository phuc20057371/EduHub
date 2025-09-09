package com.example.eduhubvn.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerProfileDTO {
    private PartnerInfoDTO partner;
    private PartnerOrganizationUpdateDTO partnerUpdate;

    
}
