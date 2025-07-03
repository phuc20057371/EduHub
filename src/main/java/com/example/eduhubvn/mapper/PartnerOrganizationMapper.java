package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerOrganizationReq;
import com.example.eduhubvn.entities.PartnerOrganization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartnerOrganizationMapper {

    PartnerOrganizationDTO toDTO(PartnerOrganization entity);
    PartnerOrganization toEntity(PartnerOrganizationDTO dto);
    PartnerOrganization toEntity(PartnerOrganizationReq req);
}
