package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.AttendedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.request.AttendedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.AttendedTrainingCourseUpdateReq;
import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.AttendedTrainingCourseUpdate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttendedTrainingCourseMapper {
    AttendedTrainingCourse toEntity(AttendedTrainingCourseReq req);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AttendedTrainingCourseUpdate toUpdate(AttendedTrainingCourseUpdateReq req);

    AttendedTrainingCourseDTO toDTO(AttendedTrainingCourse course);
    AttendedTrainingCourseDTO toDTO(AttendedTrainingCourseUpdate course);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromReq(AttendedTrainingCourseUpdateReq req,@MappingTarget AttendedTrainingCourse course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(AttendedTrainingCourseUpdate update,@MappingTarget AttendedTrainingCourse original);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUpdateFromRequest(AttendedTrainingCourseUpdateReq req,@MappingTarget AttendedTrainingCourseUpdate update);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    List<AttendedTrainingCourseDTO> toDTOs(List<AttendedTrainingCourse> attendedTrainingCourses);
}
