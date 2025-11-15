package com.example.eduhubvn.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.project.response.CourseGuestDTO;
import com.example.eduhubvn.services.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get-all-courses")
    public ResponseEntity<ApiResponse<List<CourseGuestDTO>>> getAllCourses() {
        List<CourseGuestDTO> courses = projectService.getAllCourseGuestDTO();
        return ResponseEntity.ok(ApiResponse.success("Danh sách khóa học", courses));
    }

}
