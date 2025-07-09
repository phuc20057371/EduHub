package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.OwnedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.request.OwnedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedTrainingCourseUpdateReq;
import com.example.eduhubvn.entities.OwnedTrainingCourse;
import com.example.eduhubvn.entities.OwnedTrainingCourseUpdate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OwnedTrainingCourseMapper {

    OwnedTrainingCourse toEntity(OwnedTrainingCourseReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    OwnedTrainingCourseUpdate toEntity(OwnedTrainingCourseUpdateReq req);

    OwnedTrainingCourseDTO toDTO(OwnedTrainingCourse course);
    OwnedTrainingCourseDTO toDTO(OwnedTrainingCourseUpdate course);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(OwnedTrainingCourseUpdateReq req, @MappingTarget OwnedTrainingCourse course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateUpdateFromRequest(OwnedTrainingCourseUpdateReq req, @MappingTarget OwnedTrainingCourseUpdate course);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(OwnedTrainingCourseUpdate update,@MappingTarget OwnedTrainingCourse original);
}
