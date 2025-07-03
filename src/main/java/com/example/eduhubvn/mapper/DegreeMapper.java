package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeReq;
import com.example.eduhubvn.entities.Degree;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DegreeMapper {
    DegreeDTO toDTO(Degree entity);
    Degree toEntity(DegreeDTO dto);
    Degree toEntity(CertificationReq entity);

    List<Degree> toEntities(List<DegreeReq> reqs);
    List<DegreeDTO> toDTOs(List<Degree> entities);
}
