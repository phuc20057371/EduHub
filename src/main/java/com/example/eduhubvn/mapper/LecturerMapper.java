package com.example.eduhubvn.mapper;


import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.request.LecturerReq;
import com.example.eduhubvn.dtos.lecturer.request.LecturerUpdateReq;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.LecturerUpdate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LecturerMapper {

    LecturerDTO toDTO(Lecturer lecturer);
    LecturerDTO toDTOFromUpdate(LecturerUpdate update);
    LecturerUpdateDTO toDTO(LecturerUpdate update);


    Lecturer toEntity(LecturerDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Lecturer toEntity(LecturerReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LecturerUpdate toUpdate(LecturerUpdateReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUpdateFromRequest(LecturerUpdateReq dto, @MappingTarget LecturerUpdate entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(LecturerUpdate update,@MappingTarget Lecturer lecturer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(LecturerUpdateDTO update,@MappingTarget Lecturer lecturer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(LecturerReq req, @MappingTarget  Lecturer lecturer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<LecturerDTO> toDTOs(List<Lecturer> pending);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(LecturerDTO dto, @MappingTarget Lecturer lecturer);
}