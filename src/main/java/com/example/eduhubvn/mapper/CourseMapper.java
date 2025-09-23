package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.course.CourseDTO;
import com.example.eduhubvn.dtos.course.CourseReq;
import com.example.eduhubvn.entities.Course;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO toDTO(Course course);

    List<CourseDTO> toDTOs(List<Course> courses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "courseLecturers", ignore = true)
    @Mapping(target = "ownedTrainingCourse", ignore = true)
    Course toEntity(CourseReq req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "courseLecturers", ignore = true)
    @Mapping(target = "ownedTrainingCourse", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(CourseDTO req,@MappingTarget Course course);
}
