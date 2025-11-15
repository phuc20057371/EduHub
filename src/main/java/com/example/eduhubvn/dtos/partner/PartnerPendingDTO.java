package com.example.eduhubvn.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerPendingDTO {
    private PartnerDTO partnerOrganization;
    private PartnerUpdateDTO partnerOrganizationUpdate;
}
