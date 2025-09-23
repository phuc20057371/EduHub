package com.example.eduhubvn.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.course.CourseInfoDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.services.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private final AdminService adminService;

    @GetMapping("/get-all-courses")
    public ResponseEntity<ApiResponse<List<CourseInfoDTO>>> getAllCourses() {
        List<CourseInfoDTO> courses = adminService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success("Danh sách khóa học", courses));
    }

    @GetMapping("/get-all-lecturers")
    public ResponseEntity<ApiResponse<List<LecturerInfoDTO>>> getAllLecturers() {
        List<LecturerInfoDTO> lecturers = adminService.getAllLecturers();
        return ResponseEntity.ok(ApiResponse.success("Danh sách giảng viên", lecturers));
    }

}
