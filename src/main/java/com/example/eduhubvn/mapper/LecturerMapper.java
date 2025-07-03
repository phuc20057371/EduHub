package com.example.eduhubvn.mapper;


import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.request.LecturerReq;
import com.example.eduhubvn.entities.Lecturer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LecturerMapper {

    LecturerDTO toDTO(Lecturer lecturer);
    Lecturer toEntity(LecturerDTO dto);
    Lecturer toEntity(LecturerReq req);
}