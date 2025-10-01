package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.edu.request.EduInsUpdateReq;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.EducationInstitutionUpdate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EducationInstitutionMapper {

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        EducationInstitutionDTO toDTO(EducationInstitution entity);

        EducationInstitutionUpdateDTO toDTO(EducationInstitutionUpdate entity);

        @Mapping(target = "user", ignore = true)
        @Mapping(target = "institutionUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        EducationInstitution toEntity(EducationInstitutionDTO dto);

        @Mapping(target = "user", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "institutionUpdate", ignore = true)
        @Mapping(target = "hidden", ignore = true)
        @Mapping(target = "projects", ignore = true)
        EducationInstitution toEntity(EducationInstitutionReq req);

        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "educationInstitution", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        EducationInstitutionUpdate toUpdate(EduInsUpdateReq req);

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
        void updateEntityFromRequest(EducationInstitutionReq req, @MappingTarget EducationInstitution institution);

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
        void updateEntityFromUpdate(EducationInstitutionUpdateDTO update,
                        @MappingTarget EducationInstitution educationInstitution);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "adminNote", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "educationInstitution", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateUpdateFromRequest(EduInsUpdateReq req, @MappingTarget EducationInstitutionUpdate update);

}
