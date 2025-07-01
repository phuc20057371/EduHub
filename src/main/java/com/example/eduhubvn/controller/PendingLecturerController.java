package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.lecturer.PendingCertificationDTO;
import com.example.eduhubvn.dtos.lecturer.PendingDegreeDTO;
import com.example.eduhubvn.dtos.lecturer.PendingLecturerDTO;
import com.example.eduhubvn.dtos.lecturer.request.PendingCertificationRequest;
import com.example.eduhubvn.dtos.lecturer.request.PendingDegreeRequest;
import com.example.eduhubvn.dtos.lecturer.request.PendingLecturerUpdateRequest;
import com.example.eduhubvn.dtos.lecturer.request.PendingLecturerUpdateResponse;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.services.PendingLecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/pending-lecturer")
@RequiredArgsConstructor
public class PendingLecturerController {
    private final PendingLecturerService pendingLecturerService;

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy người dùng đang đăng nhập");
        }
        return user;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PendingLecturerDTO>> createPendingLecturer(@RequestBody PendingLecturerDTO request) {
        PendingLecturerDTO pending = pendingLecturerService.createPendingLecturer(request);
        return ResponseEntity.ok(ApiResponse.success("Tạo hồ sơ thành công", pending));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<PendingLecturerUpdateResponse>> updatePendingLecturer(@RequestBody PendingLecturerUpdateRequest request) {
        PendingLecturerDTO pending = pendingLecturerService.updatePendingLecturer(request);
        PendingLecturerUpdateResponse response = LecturerMapper.toPendingLecturerUpdateResponse(pending);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật hồ sơ thành công", response));
    }

    @PostMapping("/resubmit-pending-lecturer")
    public ResponseEntity<ApiResponse<PendingLecturerDTO>> resubmitPendingLecturer(@RequestBody PendingLecturerDTO request) {
        PendingLecturerDTO updated = pendingLecturerService.resubmitPendingLecturer(request, getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Gửi lại hồ sơ thành công", updated));
    }

    @PostMapping("/pending-degree/create")
    public ResponseEntity<ApiResponse<PendingDegreeDTO>> createPendingDegree(@RequestBody PendingDegreeRequest request) {
        PendingDegreeDTO result = pendingLecturerService.createPendingDegree(request, getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Tạo bằng cấp thành công", result));
    }

    @PostMapping("/pending-degree/update")
    public ResponseEntity<ApiResponse<PendingDegreeDTO>> updatePendingDegree(@RequestBody PendingDegreeRequest request) {
        PendingDegreeDTO updated = pendingLecturerService.updatePendingDegree(request, getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Cập nhật bằng cấp thành công", updated));
    }

    @PostMapping("/pending-certification/create")
    public ResponseEntity<ApiResponse<PendingCertificationDTO>> createPendingCertification(@RequestBody PendingCertificationRequest request) {
        PendingCertificationDTO result = pendingLecturerService.createPendingCertification(request, getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Tạo chứng chỉ thành công", result));
    }

    @PostMapping("/pending-certification/update")
    public ResponseEntity<ApiResponse<PendingCertificationDTO>> updatePendingCertification(@RequestBody PendingCertificationRequest request) {
        PendingCertificationDTO updated = pendingLecturerService.updatePendingCertification(request, getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Cập nhật chứng chỉ thành công", updated));
    }








}
