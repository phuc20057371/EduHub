package com.example.eduhubvn.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.dtos.project.Response.CourseGuestDTO;
import com.example.eduhubvn.dtos.project.Response.CourseModuleGuestDTO;
import com.example.eduhubvn.entities.CourseModule;
import com.example.eduhubvn.entities.Project;
import com.example.eduhubvn.entities.ProjectCategory;
import com.example.eduhubvn.repositories.ProjectRespository;
import com.example.eduhubvn.ulti.Mapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRespository projectRepository;

    public List<CourseGuestDTO> getAllCourseGuestDTO() {
        List<CourseGuestDTO> courseGuestDTOs = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();

        for (Project project : projects) {
            if (project.getType() == ProjectCategory.COURSE) {
                CourseGuestDTO courseGuestDTO = CourseGuestDTO.builder()
                        .id(project.getId())
                        .publicTitle(project.getTitle())
                        .thumbnailUrl(project.getCourseInfo().getThumbnailUrl())
                        .introduce(project.getCourseInfo().getIntroduce())
                        .publicDescription(project.getCourseInfo().getPublicDescription())
                        .level(project.getCourseInfo().getLevel())
                        .knowledge(project.getCourseInfo().getKnowledge())
                        .requirements(project.getCourseInfo().getRequirements())
                        .published(project.getCourseInfo().isPublished())
                        .price(project.getCourseInfo().getPrice())
                        .address(project.getCourseInfo().getAddress())
                        .isOnline(project.getCourseInfo().isOnline())
                        .courseModules(getCourseModules(project.getCourseModules()))
                        .build();
                courseGuestDTOs.add(courseGuestDTO);
            }

        }
        return courseGuestDTOs;
    }

    private List<CourseModuleGuestDTO> getCourseModules(List<CourseModule> courseModules) {
        List<CourseModuleGuestDTO> courseModuleGuestDTOs = new ArrayList<>();
        for (CourseModule courseModule : courseModules) {
            // Tìm lecturer từ ApplicationModule đầu tiên (tất cả ApplicationModules đều liên kết với Application APPROVED)
            LecturerInfoDTO lecturerInfoDTO = courseModule.getApplicationModules().stream()
                    .filter(appModule -> appModule.getApplication() != null)
                    .findFirst()
                    .map(appModule -> Mapper.mapToLecturerInfoDTO(appModule.getApplication().getLecturer()))
                    .orElse(null);
            
            CourseModuleGuestDTO courseModuleGuestDTO = CourseModuleGuestDTO.builder()
                    .id(courseModule.getId())
                    .title(courseModule.getTitle())
                    .description(courseModule.getDescription())
                    .duration(courseModule.getDuration())
                    .moduleOrder(courseModule.getModuleOrder())
                    .lecturer(lecturerInfoDTO)
                    .build();
            courseModuleGuestDTOs.add(courseModuleGuestDTO);
        }
        return courseModuleGuestDTOs;
    }
}
