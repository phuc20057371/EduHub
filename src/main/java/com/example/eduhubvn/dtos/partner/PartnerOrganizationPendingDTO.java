package com.example.eduhubvn.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerOrganizationPendingDTO {
    private PartnerOrganizationDTO partnerOrganization;
    private PartnerOrganizationUpdateDTO partnerOrganizationUpdate;
}
