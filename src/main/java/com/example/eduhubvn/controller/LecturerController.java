package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.lecturer.AttendedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
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
public class LecturerController {

    private final LecturerService lecturerService;

    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse<LecturerUpdateReq>> updateLecturerFromUser(@RequestBody LecturerUpdateReq req, @AuthenticationPrincipal User user) {
        LecturerUpdateReq request = lecturerService.updateLecturerFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", request));
    }
    @PostMapping("/update-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> updateDegreeFromUser(@RequestBody DegreeUpdateReq req, @AuthenticationPrincipal User user) {
        DegreeDTO dto = lecturerService.updateDegreeFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/update-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> updateCertificationFromUser(@RequestBody CertificationUpdateReq req, @AuthenticationPrincipal User user) {
        CertificationDTO dto = lecturerService.updateCertificationFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/edit-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> editDegreeFromUser(@RequestBody DegreeUpdateReq req, @AuthenticationPrincipal User user) {
        DegreeDTO dto = lecturerService.editDegreeFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @PostMapping("/edit-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> editCertificationFromUser(@RequestBody CertificationUpdateReq req, @AuthenticationPrincipal User user) {
        CertificationDTO dto = lecturerService.editCertificationFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/create-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> createAttendedCourse(@RequestBody AttendedTrainingCourseReq req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.createAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> updateAttendedCourse(@RequestBody AttendedTrainingCourseUpdateReq req, @AuthenticationPrincipal User user) {
        AttendedTrainingCourseDTO dto = lecturerService.updateAttendedCourse(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
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
