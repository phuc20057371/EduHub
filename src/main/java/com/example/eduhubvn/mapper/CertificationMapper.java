package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.CertificationUpdateReq;
import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.CertificationUpdate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificationMapper {

    CertificationDTO toDTO (Certification entity);
    CertificationDTO toDTO (CertificationUpdate entity);
    Certification toEntity (CertificationDTO dto);
    Certification toEntity (CertificationReq req);

    List<CertificationDTO> toDTOs (List<Certification> entities);
    List<Certification> toEntities(List<CertificationReq> reqs);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromReq(CertificationUpdateReq req, @MappingTarget Certification certification);
}
