package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerOrganizationReq;
import com.example.eduhubvn.dtos.partner.request.PartnerUpdateReq;
import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PartnerOrganizationUpdate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PartnerOrganizationMapper {

    PartnerOrganizationDTO toDTO(PartnerOrganization entity);
    PartnerOrganizationUpdateDTO toDTO(PartnerOrganizationUpdate entity);
    PartnerOrganization toEntity(PartnerOrganizationDTO dto);
    PartnerOrganization toEntity(PartnerOrganizationReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(PartnerOrganizationReq req,@MappingTarget PartnerOrganization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(PartnerOrganizationUpdate update,@MappingTarget PartnerOrganization partnerOrganization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(PartnerOrganizationUpdateDTO update,@MappingTarget PartnerOrganization partnerOrganization);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PartnerOrganizationUpdate toUpdate(PartnerUpdateReq req);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUpdateFromRequest(PartnerUpdateReq req,@MappingTarget PartnerOrganizationUpdate update);
}
