package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.entities.EducationInstitution;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EducationInstitutionMapper {

    EducationInstitutionDTO toDTO(EducationInstitution entity);
    EducationInstitution toEntity(EducationInstitutionDTO dto);
    EducationInstitution toEntity(EducationInstitutionReq req);

}
