package com.example.eduhubvn.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.dtos.project.Response.CourseGuestDTO;
import com.example.eduhubvn.services.AdminService;
import com.example.eduhubvn.services.LecturerService;
import com.example.eduhubvn.services.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private final AdminService adminService;
    private final ProjectService projectService;
    private final LecturerService lecturerService;


    @GetMapping("/get-all-lecturers")
    public ResponseEntity<ApiResponse<List<LecturerInfoDTO>>> getAllLecturers() {
        List<LecturerInfoDTO> lecturers = adminService.getAllLecturers();
        return ResponseEntity.ok(ApiResponse.success("Danh sách giảng viên", lecturers));
    }

    @GetMapping("/all-courses")
    public ResponseEntity<ApiResponse<List<CourseGuestDTO>>> getAllCoursesFormPrject() {
        List<CourseGuestDTO> courses = projectService.getAllCourseGuestDTO();
        return ResponseEntity.ok(ApiResponse.success("Danh sách khóa học", courses));
    }

    @GetMapping("/top-7-lecturers")
    public ResponseEntity<ApiResponse<List<LecturerDTO>>> getTop7Lecturers() {
        List<LecturerDTO> lecturers = lecturerService.getTop7Lecturers();
        return ResponseEntity.ok(ApiResponse.success("Danh sách giảng viên", lecturers));
    }

}
