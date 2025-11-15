package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.AttendedCourseDTO;
import com.example.eduhubvn.dtos.lecturer.request.AttendedCourseCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.AttendedCourseUpdateReq;
import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.AttendedTrainingCourseUpdate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AttendedCourseMapper {

    AttendedTrainingCourse toEntity(AttendedCourseCreateReq req);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AttendedTrainingCourseUpdate toUpdate(AttendedCourseUpdateReq req);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "adminNote", source = "adminNote")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    AttendedCourseDTO toDTO(AttendedTrainingCourse course);

    AttendedCourseDTO toDTO(AttendedTrainingCourseUpdate course);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromReq(AttendedCourseUpdateReq req, @MappingTarget AttendedTrainingCourse course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    @Mapping(target = "attendedTrainingCourseUpdate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(AttendedTrainingCourseUpdate update, @MappingTarget AttendedTrainingCourse original);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUpdateFromRequest(AttendedCourseUpdateReq req, @MappingTarget AttendedTrainingCourseUpdate update);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    List<AttendedCourseDTO> toDTOs(List<AttendedTrainingCourse> attendedTrainingCourses);
}
