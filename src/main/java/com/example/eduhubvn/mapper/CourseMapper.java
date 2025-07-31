package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.course.CourseDTO;
import com.example.eduhubvn.dtos.course.CourseReq;
import com.example.eduhubvn.entities.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO toDTO(Course course);

    Course toEntity(CourseReq req);
}
