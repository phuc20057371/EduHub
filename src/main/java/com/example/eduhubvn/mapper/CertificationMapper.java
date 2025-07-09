package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.CertificationUpdateReq;
import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.CertificationUpdate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificationMapper {

    CertificationDTO toDTO (Certification entity);
    CertificationDTO toDTO (CertificationUpdate entity);

    Certification toEntity (CertificationDTO dto);
    Certification toEntity (CertificationReq req);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CertificationUpdate toUpdate(CertificationUpdateReq req);

    List<CertificationDTO> toDTOs (List<Certification> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<Certification> toEntities(List<CertificationReq> reqs);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromReq(CertificationUpdateReq req, @MappingTarget Certification certification);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(CertificationUpdate source, @MappingTarget Certification target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUpdateFromRequest(CertificationUpdateReq req,@MappingTarget CertificationUpdate update);


}
