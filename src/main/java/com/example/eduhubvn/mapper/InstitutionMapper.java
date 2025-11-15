package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.institution.InstitutionDTO;
import com.example.eduhubvn.dtos.institution.InstitutionUpdateDTO;
import com.example.eduhubvn.dtos.institution.request.InstitutionCreateReq;
import com.example.eduhubvn.dtos.institution.request.InstitutionUpdateReq;
import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.EducationInstitutionUpdate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        InstitutionDTO toDTO(EducationInstitution entity);

        InstitutionUpdateDTO toDTO(EducationInstitutionUpdate entity);

        @Mapping(target = "user", ignore = true)
        @Mapping(target = "institutionUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        EducationInstitution toEntity(InstitutionDTO dto);

        @Mapping(target = "user", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "institutionUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        EducationInstitution toEntity(InstitutionCreateReq req);

        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "educationInstitution", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        EducationInstitutionUpdate toUpdate(InstitutionUpdateReq req);

        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "user", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        @Mapping(target = "institutionUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        void updateEntityFromRequest(InstitutionCreateReq req, @MappingTarget EducationInstitution institution);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "user", ignore = true)
        @Mapping(target = "institutionUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateEntityFromUpdate(EducationInstitutionUpdate update,
                        @MappingTarget EducationInstitution educationInstitution);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "user", ignore = true)
        @Mapping(target = "institutionUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateEntityFromUpdate(InstitutionUpdateDTO update,
                        @MappingTarget EducationInstitution educationInstitution);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "educationInstitution", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateUpdateFromRequest(InstitutionUpdateReq req, @MappingTarget EducationInstitutionUpdate update);

}
