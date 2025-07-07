package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.LecturerUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.request.LecturerUpdateReq;
import com.example.eduhubvn.entities.LecturerUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LecturerUpdateMapper {
    LecturerUpdateReq toReq(LecturerUpdate request);
    LecturerUpdate toUpdate(LecturerUpdateReq req);

    void updateEntityFromDto(LecturerUpdateReq dto, @MappingTarget LecturerUpdate entity);

    LecturerUpdateDTO toDTO(LecturerUpdate update);
}
