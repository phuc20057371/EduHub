package com.example.eduhubvn.mapper;

import org.mapstruct.Mapper;

import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.dtos.project.Response.CourseModuleGuestDTO;
import com.example.eduhubvn.entities.CourseModule;
import com.example.eduhubvn.entities.Lecturer;

@Mapper(componentModel = "spring")
public abstract class ProjectMapper {

    public CourseModuleGuestDTO toCourseModuleGuestDTO(CourseModule courseModule) {
        if (courseModule == null) {
            return null;
        }

        return CourseModuleGuestDTO.builder()
                .id(courseModule.getId())
                .title(courseModule.getTitle())
                .description(courseModule.getDescription())
                .duration(courseModule.getDuration())
                .moduleOrder(courseModule.getModuleOrder())
                .lecturer(getLecturerInfoDTO(courseModule))
                .createdAt(courseModule.getCreatedAt())
                .updatedAt(courseModule.getUpdatedAt())
                .build();
    }

    private LecturerInfoDTO getLecturerInfoDTO(CourseModule courseModule) {
        if (courseModule == null || courseModule.getApplicationModules() == null) {
            return null;
        }

        // Tìm lecturer từ ApplicationModule đầu tiên (tất cả ApplicationModules đều liên kết với Application APPROVED)
        Lecturer lecturer = courseModule.getApplicationModules().stream()
                .filter(appModule -> appModule.getApplication() != null)
                .findFirst()
                .map(appModule -> appModule.getApplication().getLecturer())
                .orElse(null);

        return com.example.eduhubvn.ulti.Mapper.mapToLecturerInfoDTO(lecturer);
    }
}
