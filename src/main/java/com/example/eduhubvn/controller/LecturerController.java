package com.example.eduhubvn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.BooleanRequest;
import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.lecturer.AttendedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerProfileDTO;
import com.example.eduhubvn.dtos.lecturer.OwnedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.ResearchProjectDTO;
import com.example.eduhubvn.dtos.lecturer.request.AttendedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.AttendedTrainingCourseUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.LecturerUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedTrainingCourseUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.ResearchProjectReq;
import com.example.eduhubvn.dtos.lecturer.request.ResearchProjectUpdateReq;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.LecturerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lecturer")
@PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerService lecturerService;

    @GetMapping("/lecturer-profile")
    public ResponseEntity<ApiResponse<LecturerProfileDTO>> getLecturerProfile(@AuthenticationPrincipal User user) {
        LecturerProfileDTO lecturer = lecturerService.getLecturerProfile(user.getLecturer().getId());
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin giảng viên thành công", lecturer));
    }

    /// Lecturer
    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse<LecturerDTO>> updateLecturerProfile(@RequestBody LecturerUpdateReq req,
            @AuthenticationPrincipal User user) {
        LecturerDTO request = lecturerService.updateLecturerProfile(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", request));
    }

    @PostMapping("/hidden-profile")
    public ResponseEntity<ApiResponse<LecturerDTO>> hiddenLecturerProfile(@AuthenticationPrincipal User user,
            @RequestBody BooleanRequest hidden) {
        LecturerDTO request = lecturerService.hiddenLecturerProfile(user, hidden);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu ẩn thông tin", request));
    }

    /// Certification
    /// Degree

    /// Attended Training Course
    @PostMapping("/create-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> createAttendedCourse(
            @RequestBody AttendedTrainingCourseReq req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.createAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/update-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> updateAttendedCourse(
            @RequestBody AttendedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.updateAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/edit-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> editAttendedCourse(
            @RequestBody AttendedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.editAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/delete-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> deleteAttendedCourse(
            @RequestBody IdRequest req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.deleteAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa", dto));
    }

    /// Owned Training Course
    @PostMapping("/create-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> createOwnedCourse(
            @RequestBody OwnedTrainingCourseReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.createOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/update-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> updateOwnedCourse(
            @RequestBody OwnedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.updateOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/edit-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> editOwnedCourse(
            @RequestBody OwnedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.editOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/delete-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> deleteOwnedCourse(
            @RequestBody IdRequest req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.deleteOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa", dto));
    }

    /// Research Project
    @PostMapping("/create-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> createResearchProject(@RequestBody ResearchProjectReq req,
            @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.createResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/update-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> updateResearchProject(
            @RequestBody ResearchProjectUpdateReq req, @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.updateResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/edit-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> editResearchProject(
            @RequestBody ResearchProjectUpdateReq req, @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.editResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/delete-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> deleteResearchProject(
            @RequestBody IdRequest req, @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.deleteResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa", dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('lecturer:read')")
    public String get() {
        return "GET:: LECTURER";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('lecturer:create')")
    public String post() {
        return "POST:: LECTURER";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('lecturer:update')")
    public String put() {
        return "PUT:: LECTURER";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('lecturer:delete')")
    public String delete() {
        return "DELETE:: LECTURER";
    }

}
