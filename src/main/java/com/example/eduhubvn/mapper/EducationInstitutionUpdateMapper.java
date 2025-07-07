package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.edu.request.EduInsUpdateReq;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.entities.EducationInstitutionUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface  EducationInstitutionUpdateMapper {
    EducationInstitutionUpdate toUpdate(EduInsUpdateReq req);

    void updateEntityFromDto(EduInsUpdateReq dto, @MappingTarget EducationInstitutionUpdate entity);

    EducationInstitutionUpdateDTO toDTO(EducationInstitutionUpdate update);
}
