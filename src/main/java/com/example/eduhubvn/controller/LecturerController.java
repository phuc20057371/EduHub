package com.example.eduhubvn.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.BooleanRequest;
import com.example.eduhubvn.dtos.IdReq;
import com.example.eduhubvn.dtos.lecturer.AttendedCourseDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerProfileDTO;
import com.example.eduhubvn.dtos.lecturer.OwnedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.ResearchProjectDTO;
import com.example.eduhubvn.dtos.lecturer.request.AttendedCourseCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.AttendedCourseUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.LecturerUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedCourseCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedCourseUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.ResearchProjectCreateReq;
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

    /// Lecturer

    @GetMapping("/lecturer-profile")
    public ResponseEntity<ApiResponse<LecturerProfileDTO>> getLecturerProfile(@AuthenticationPrincipal User user) {
        LecturerProfileDTO lecturer = lecturerService.getLecturerProfile(user.getLecturer().getId());
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin giảng viên thành công", lecturer));
    }

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
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> createAttendedCourse(
            @RequestBody AttendedCourseCreateReq req, @AuthenticationPrincipal User user) {
        AttendedCourseDTO dto = lecturerService.createAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:update'))")
    @PostMapping("/update-attended-course")
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> updateAttendedCourse(
            @RequestBody AttendedCourseUpdateReq req, @AuthenticationPrincipal User user) {
        AttendedCourseDTO dto = lecturerService.updateAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/edit-attended-course")
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> editAttendedCourse(
            @RequestBody AttendedCourseUpdateReq req, @AuthenticationPrincipal User user) {
        AttendedCourseDTO dto = lecturerService.editAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/delete-attended-course")
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> deleteAttendedCourse(
            @RequestBody IdReq req, @AuthenticationPrincipal User user) {
        AttendedCourseDTO dto = lecturerService.deleteAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa", dto));
    }

    /// Owned Training Course
    @PostMapping("/create-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> createOwnedCourse(
            @RequestBody OwnedCourseCreateReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.createOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:update'))")
    @PostMapping("/update-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> updateOwnedCourse(
            @RequestBody OwnedCourseUpdateReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.updateOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/edit-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> editOwnedCourse(
            @RequestBody OwnedCourseUpdateReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.editOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/delete-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> deleteOwnedCourse(
            @RequestBody IdReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.deleteOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa", dto));
    }

    /// Research Project
    @PostMapping("/create-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> createResearchProject(@RequestBody ResearchProjectCreateReq req,
            @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.createResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:update'))")
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
            @RequestBody IdReq req, @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.deleteResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa", dto));
    }

    /// Course

    /// Avatar
    @PostMapping("/update-avatar")
    public ResponseEntity<ApiResponse<LecturerDTO>> updateLecturerAvatar(@RequestParam("file") MultipartFile file,
            HttpServletRequest request, @AuthenticationPrincipal User user) {

        // Validate file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File không được trống", null));
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File phải có định dạng .jpg, .jpeg hoặc .png", null));
        }

        try {
            // Create upload directory
            Path uploadPath = Paths.get("uploads", String.valueOf(user.getRole()), String.valueOf(user.getId()));
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Build file access URL
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String fileUrl = baseUrl + "/uploads/" + user.getRole() + "/" + user.getId() + "/" + fileName;

            // Update lecturer avatar
            LecturerDTO dto = lecturerService.updateLecturerAvatar(fileUrl, user);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật avatar thành công", dto));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi upload file: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi cập nhật avatar: " + e.getMessage(), null));
        }
    }

}
