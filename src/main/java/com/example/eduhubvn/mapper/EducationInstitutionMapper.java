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

    EducationInstitutionDTO toDTO(EducationInstitution entity);
    EducationInstitutionUpdateDTO toDTO(EducationInstitutionUpdate entity);

    EducationInstitution toEntity(EducationInstitutionDTO dto);
    EducationInstitution toEntity(EducationInstitutionReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EducationInstitutionUpdate toUpdate(EduInsUpdateReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(EducationInstitutionReq req, @MappingTarget EducationInstitution institution);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(EducationInstitutionUpdate update, @MappingTarget EducationInstitution educationInstitution);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUpdateFromRequest(EduInsUpdateReq req,@MappingTarget EducationInstitutionUpdate update);


}
