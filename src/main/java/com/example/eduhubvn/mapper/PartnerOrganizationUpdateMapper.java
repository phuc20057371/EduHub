package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerUpdateReq;
import com.example.eduhubvn.entities.PartnerOrganizationUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PartnerOrganizationUpdateMapper {
    PartnerOrganizationUpdate toUpdate(PartnerUpdateReq req);

    void updateEntityFromDto(PartnerUpdateReq req, @MappingTarget PartnerOrganizationUpdate update);

    PartnerOrganizationUpdateDTO toDTO(PartnerOrganizationUpdate update);
}
