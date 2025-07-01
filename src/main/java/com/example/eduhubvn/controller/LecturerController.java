package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.pendingCourse.PendingAttendedTrainingCourseRequest;
import com.example.eduhubvn.dtos.lecturer.pendingCourse.PendingOwnedTrainingCourseRequest;
import com.example.eduhubvn.dtos.lecturer.pendingCourse.PendingResearchProjectRequest;
import com.example.eduhubvn.entities.PendingAttendedTrainingCourse;
import com.example.eduhubvn.entities.PendingOwnedTrainingCourse;
import com.example.eduhubvn.entities.PendingResearchProject;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.services.LecturerService;
import com.example.eduhubvn.services.PendingLecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturer")
@PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerService lecturerService;
    private final PendingLecturerService pendingLecturerService;

    @GetMapping("/{lecturerId}/courses")
    public ResponseEntity<ApiResponse<LecturerCourseDTO>> getLecturerCourses(@PathVariable Integer lecturerId) {
        LecturerCourseDTO dto = lecturerService.getLecturerCourses(lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Lấy khóa học thành công", dto));
    }

    @PostMapping("/add-owned-course")
    public ResponseEntity<ApiResponse<PendingOwnedTrainingCourseDTO>> addPendingOwnedTrainingCourse(
            @RequestBody PendingOwnedTrainingCourseRequest request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }

        PendingOwnedTrainingCourseDTO dto = pendingLecturerService.addOwnedCourse(request, user);
        return ResponseEntity.ok(ApiResponse.success("Thêm khóa học giảng dạy thành công", dto));
    }

    @PostMapping("/add-attended-course")
    public ResponseEntity<ApiResponse<PendingAttendedTrainingCourseDTO>> addPendingAttendedTrainingCourse(
            @RequestBody PendingAttendedTrainingCourseRequest request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }

        PendingAttendedTrainingCourseDTO dto = pendingLecturerService.addAttendedCourse(request, user);
        return ResponseEntity.ok(ApiResponse.success("Thêm khóa học tham gia thành công", dto));
    }

    @PostMapping("/add-research-project")
    public ResponseEntity<ApiResponse<PendingResearchProjectDTO>> addPendingResearchProject(
            @RequestBody PendingResearchProjectRequest request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }

        PendingResearchProjectDTO dto = pendingLecturerService.addResearchProject(request, user);
        return ResponseEntity.ok(ApiResponse.success("Thêm đề tài nghiên cứu thành công", dto));
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
