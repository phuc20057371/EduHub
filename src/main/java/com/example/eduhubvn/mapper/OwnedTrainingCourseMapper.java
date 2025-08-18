package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.OwnedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.request.OwnedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedTrainingCourseUpdateReq;
import com.example.eduhubvn.entities.OwnedTrainingCourse;
import com.example.eduhubvn.entities.OwnedTrainingCourseUpdate;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnedTrainingCourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OwnedTrainingCourse toEntity(OwnedTrainingCourseReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "ownedTrainingCourse", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OwnedTrainingCourseUpdate toEntity(OwnedTrainingCourseUpdateReq req);

    OwnedTrainingCourseDTO toDTO(OwnedTrainingCourse course);

    OwnedTrainingCourseDTO toDTO(OwnedTrainingCourseUpdate course);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "adminNote", ignore = true),
            @Mapping(target = "course", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lecturer", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(OwnedTrainingCourseUpdateReq req, @MappingTarget OwnedTrainingCourse course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "ownedTrainingCourse", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUpdateFromRequest(OwnedTrainingCourseUpdateReq req, @MappingTarget OwnedTrainingCourseUpdate course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(OwnedTrainingCourseUpdate update, @MappingTarget OwnedTrainingCourse original);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNote", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    List<OwnedTrainingCourseDTO> toDTOs(List<OwnedTrainingCourse> ownedTrainingCourses);
}
