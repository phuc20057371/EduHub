package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeUpdateReq;
import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.DegreeUpdate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DegreeMapper {
    DegreeDTO toDTO(Degree entity);
    DegreeDTO toDTO(DegreeUpdate entity);
    Degree toEntity(DegreeDTO dto);
    Degree toEntity(CertificationReq entity);

    List<Degree> toEntities(List<DegreeReq> reqs);
    List<DegreeDTO> toDTOs(List<Degree> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromReq(DegreeUpdateReq req,@MappingTarget Degree degree);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DegreeUpdateReq req, @MappingTarget DegreeUpdate entity);

    DegreeUpdate toUpdate(DegreeUpdateReq req);
}
