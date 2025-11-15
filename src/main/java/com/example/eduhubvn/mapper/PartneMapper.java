package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.partner.PartnerDTO;
import com.example.eduhubvn.dtos.partner.PartnerUpdateDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerCreateReq;
import com.example.eduhubvn.dtos.partner.request.PartnerUpdateReq;
import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PartnerOrganizationUpdate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PartneMapper {

        PartnerDTO toDTO(PartnerOrganization entity);

        PartnerUpdateDTO toDTO(PartnerOrganizationUpdate entity);

        @Mapping(target = "user", ignore = true)
        @Mapping(target = "partnerUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        @Mapping(target = "trainingProgramRequests", ignore = true)
        @Mapping(target = "trainingPrograms", ignore = true)
        PartnerOrganization toEntity(PartnerDTO dto);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "user", ignore = true)
        @Mapping(target = "partnerUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        @Mapping(target = "trainingProgramRequests", ignore = true)
        @Mapping(target = "trainingPrograms", ignore = true)
        PartnerOrganization toEntity(PartnerCreateReq req);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "user", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        @Mapping(target = "partnerUpdate", ignore = true)
        @Mapping(target = "projects", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "trainingProgramRequests", ignore = true)
        @Mapping(target = "trainingPrograms", ignore = true)
        void updateEntityFromRequest(PartnerCreateReq req, @MappingTarget PartnerOrganization organization);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "user", ignore = true)
        @Mapping(target = "trainingProgramRequests", ignore = true)
        @Mapping(target = "trainingPrograms", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        @Mapping(target = "partnerUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        void updateEntityFromUpdate(PartnerOrganizationUpdate update,
                        @MappingTarget PartnerOrganization partnerOrganization);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "user", ignore = true)
        @Mapping(target = "trainingProgramRequests", ignore = true)
        @Mapping(target = "trainingPrograms", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        @Mapping(target = "partnerUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        void updateEntityFromUpdate(PartnerUpdateDTO update,
                        @MappingTarget PartnerOrganization partnerOrganization);

        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "partnerOrganization", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        PartnerOrganizationUpdate toUpdate(PartnerUpdateReq req);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "partnerOrganization", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateUpdateFromRequest(PartnerUpdateReq req, @MappingTarget PartnerOrganizationUpdate update);
}
