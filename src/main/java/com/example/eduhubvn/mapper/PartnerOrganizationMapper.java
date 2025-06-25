package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PendingPartnerOrganization;


public class PartnerOrganizationMapper {

    public static PartnerOrganizationDTO toDTO(PartnerOrganization partnerOrganization) {
        if (partnerOrganization == null) return null;

        return PartnerOrganizationDTO.builder()
                .id(partnerOrganization.getId())
                .organizationName(partnerOrganization.getOrganizationName())
                .industry(partnerOrganization.getIndustry())
                .taxCode(partnerOrganization.getTaxCode())
                .phoneNumber(partnerOrganization.getPhoneNumber())
                .website(partnerOrganization.getWebsite())
                .address(partnerOrganization.getAddress())
                .representativeName(partnerOrganization.getRepresentativeName())
                .position(partnerOrganization.getPosition())
                .description(partnerOrganization.getDescription())
                .logoUrl(partnerOrganization.getLogoUrl())
                .establishedYear(partnerOrganization.getEstablishedYear())
                .build();
    }
    public static PendingPartnerOrganizationDTO toPendingDTO(PendingPartnerOrganization pending) {
        if (pending == null) return null;

        return PendingPartnerOrganizationDTO.builder()
                .id(pending.getId())
                .organizationName(pending.getOrganizationName())
                .industry(pending.getIndustry())
                .taxCode(pending.getTaxCode())
                .phoneNumber(pending.getPhoneNumber())
                .website(pending.getWebsite())
                .address(pending.getAddress())
                .representativeName(pending.getRepresentativeName())
                .position(pending.getPosition())
                .description(pending.getDescription())
                .logoUrl(pending.getLogoUrl())
                .establishedYear(pending.getEstablishedYear())
                .status(pending.getStatus() != null ? pending.getStatus().name() : null)
                .reason(pending.getReason())
                .createdAt(pending.getCreatedAt())
                .updatedAt(pending.getUpdatedAt())
                .build();
    }

}
