package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.entities.Certification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificationMapper {

    CertificationDTO toDTO (Certification entity);
    Certification toEntity (CertificationDTO dto);
    Certification toEntity (CertificationReq req);

    List<CertificationDTO> toDTOs (List<Certification> entities);
    List<Certification> toEntities(List<CertificationReq> reqs);
}
