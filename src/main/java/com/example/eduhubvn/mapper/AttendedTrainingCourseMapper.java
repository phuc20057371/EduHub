package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.AttendedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.request.AttendedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.AttendedTrainingCourseUpdateReq;
import com.example.eduhubvn.entities.AttendedTrainingCourse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AttendedTrainingCourseMapper {
    AttendedTrainingCourse toEntity(AttendedTrainingCourseReq req);
    AttendedTrainingCourseDTO toDTO(AttendedTrainingCourse course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromReq(AttendedTrainingCourseUpdateReq req,@MappingTarget AttendedTrainingCourse course);
}
