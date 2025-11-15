package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.LecturerBasicPublicDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.request.LecturerCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.LecturerUpdateReq;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.LecturerUpdate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LecturerMapper {

    LecturerDTO toDTO(Lecturer lecturer);

    @Mapping(target = "rating", ignore = true)
    LecturerBasicPublicDTO toBasicPublicDTO(Lecturer lecturer);

    @Mapping(target = "citizenId", source = "citizenId")
    @Mapping(target = "hidden", ignore = true)
    @Mapping(target = "lecturerId", ignore = true)
    LecturerDTO toDTOFromUpdate(LecturerUpdate update);

    LecturerUpdateDTO toDTO(LecturerUpdate update);

    @Mapping(target = "attendedTrainingCourses", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "degrees", ignore = true)
    @Mapping(target = "ownedTrainingCourses", ignore = true)
    @Mapping(target = "researchProjects", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lecturerUpdate", ignore = true)
    @Mapping(target = "applications", ignore = true)
    Lecturer toEntity(LecturerDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "attendedTrainingCourses", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "degrees", ignore = true)
    @Mapping(target = "hidden", ignore = true)
    @Mapping(target = "ownedTrainingCourses", ignore = true)
    @Mapping(target = "researchProjects", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lecturerUpdate", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "lecturerId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Lecturer toEntity(LecturerCreateReq req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "jobField", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LecturerUpdate toUpdate(LecturerUpdateReq req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUpdateFromRequest(LecturerUpdateReq dto, @MappingTarget LecturerUpdate entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "attendedTrainingCourses", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "degrees", ignore = true)
    @Mapping(target = "hidden", ignore = true)
    @Mapping(target = "ownedTrainingCourses", ignore = true)
    @Mapping(target = "researchProjects", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lecturerUpdate", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "lecturerId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(LecturerUpdate update, @MappingTarget Lecturer lecturer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "attendedTrainingCourses", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "degrees", ignore = true)
    @Mapping(target = "hidden", ignore = true)
    @Mapping(target = "ownedTrainingCourses", ignore = true)
    @Mapping(target = "researchProjects", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lecturerUpdate", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "lecturerId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(LecturerUpdateDTO update, @MappingTarget Lecturer lecturer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "attendedTrainingCourses", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "degrees", ignore = true)
    @Mapping(target = "hidden", ignore = true)
    @Mapping(target = "ownedTrainingCourses", ignore = true)
    @Mapping(target = "researchProjects", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lecturerUpdate", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "lecturerId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(LecturerCreateReq req, @MappingTarget Lecturer lecturer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<LecturerDTO> toDTOs(List<Lecturer> pending);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "attendedTrainingCourses", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "degrees", ignore = true)
    @Mapping(target = "ownedTrainingCourses", ignore = true)
    @Mapping(target = "researchProjects", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lecturerUpdate", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(LecturerDTO dto, @MappingTarget Lecturer lecturer);

}