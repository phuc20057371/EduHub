package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.BooleanRequest;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.*;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.LecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturer")
@PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class    LecturerController {

    private final LecturerService lecturerService;

    @GetMapping("/lecturer-profile")
    public ResponseEntity<ApiResponse<LecturerProfileDTO>> getLecturerProfile(@AuthenticationPrincipal User user) {
        LecturerProfileDTO lecturer = lecturerService.getLecturerProfile(user.getLecturer().getId());
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin giảng viên thành công", lecturer));
    }

/// Lecturer
    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse<LecturerDTO>> updateLecturerProfile(@RequestBody LecturerUpdateReq req, @AuthenticationPrincipal User user) {
        LecturerDTO request = lecturerService.updateLecturerProfile(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", request));
    }
    @PostMapping("/hidden-profile")
    public ResponseEntity<ApiResponse<LecturerDTO>> hiddenLecturerProfile( @AuthenticationPrincipal User user, @RequestBody BooleanRequest hidden) {
        LecturerDTO request = lecturerService.hiddenLecturerProfile(user, hidden);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu ẩn thông tin", request));
    }


/// Certification
    @PostMapping("/create-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> createCertificationFromUser(@RequestBody CertificationReq req, @AuthenticationPrincipal User user) {
        CertificationDTO dto = lecturerService.createCertificationFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> updateCertificationFromUser(@RequestBody CertificationUpdateReq req, @AuthenticationPrincipal User user) {
        CertificationDTO dto = lecturerService.updateCertificationFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/edit-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> editCertificationFromUser(@RequestBody CertificationUpdateReq req, @AuthenticationPrincipal User user) {
        CertificationDTO dto = lecturerService.editCertificationFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
/// Degree

    @PostMapping("/create-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> createDegreeFromUser(@RequestBody DegreeReq req, @AuthenticationPrincipal User user) {
        DegreeDTO dto = lecturerService.createDegreeFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> updateDegreeFromUser(@RequestBody DegreeUpdateReq req, @AuthenticationPrincipal User user) {
        DegreeDTO dto = lecturerService.updateDegreeFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/edit-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> editDegreeFromUser(@RequestBody DegreeUpdateReq req, @AuthenticationPrincipal User user) {
        DegreeDTO dto = lecturerService.editDegreeFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
/// Attended Training Course
    @PostMapping("/create-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> createAttendedCourse(@RequestBody AttendedTrainingCourseReq req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.createAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> updateAttendedCourse(@RequestBody AttendedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.updateAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/edit-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> editAttendedCourse(@RequestBody AttendedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.editAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
/// Owned Training Course
    @PostMapping("/create-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> createOwnedCourse(@RequestBody OwnedTrainingCourseReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.createOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> updateOwnedCourse(@RequestBody OwnedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.updateOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/edit-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> editOwnedCourse(@RequestBody OwnedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        OwnedTrainingCourseDTO dto = lecturerService.editOwnedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
/// Research Project
    @PostMapping("/create-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> createResearchProject(@RequestBody ResearchProjectReq req, @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.createResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> updateResearchProject(@RequestBody ResearchProjectUpdateReq req, @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.updateResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/edit-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> editResearchProject(@RequestBody ResearchProjectUpdateReq req, @AuthenticationPrincipal User user) {
        ResearchProjectDTO dto = lecturerService.editResearchProject(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }








    @GetMapping
    @PreAuthorize("hasAuthority('lecturer:read')")
    public String get(){
        return "GET:: LECTURER";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('lecturer:create')")
    public String post(){
        return "POST:: LECTURER";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('lecturer:update')")
    public String put(){
        return "PUT:: LECTURER";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('lecturer:delete')")
    public String delete(){
        return "DELETE:: LECTURER";
    }


}
