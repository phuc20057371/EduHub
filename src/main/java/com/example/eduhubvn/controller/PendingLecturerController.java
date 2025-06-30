package com.example.eduhubvn.controller;


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

    @PostMapping("/create")
    public ResponseEntity<PendingLecturerDTO> createPendingLecturer(@RequestBody PendingLecturerDTO request) {
        PendingLecturerDTO pending = pendingLecturerService.createPendingLecturer(request);
        return ResponseEntity.ok(pending);
    }
    @PostMapping("/update")
    public ResponseEntity<PendingLecturerUpdateResponse> updatePendingLecturer(@RequestBody PendingLecturerUpdateRequest request) {
        PendingLecturerDTO pending = pendingLecturerService.updatePendingLecturer(request);
        PendingLecturerUpdateResponse pendingLecturerUpdateResponse = LecturerMapper.toPendingLecturerUpdateResponse(pending);
        return ResponseEntity.ok(pendingLecturerUpdateResponse);
    }
    @PostMapping("/pending-certification/create")
    public ResponseEntity<PendingCertificationDTO> createPendingCertification(@RequestBody PendingCertificationRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        PendingCertificationDTO result = pendingLecturerService.createPendingCertification(request, user);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/pending-certification/update")
    public ResponseEntity<PendingCertificationDTO> updatePendingCertification(@RequestBody PendingCertificationRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        PendingCertificationDTO updated = pendingLecturerService.updatePendingCertification(request, user);
        return ResponseEntity.ok(updated);
    }
    @PostMapping("/pending-degree/create")
    public ResponseEntity<PendingDegreeDTO> createPendingDegree(@RequestBody PendingDegreeRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        PendingDegreeDTO result = pendingLecturerService.createPendingDegree(request, user);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/pending-degree/update")
    public ResponseEntity<PendingDegreeDTO> updatePendingDegree(@RequestBody PendingDegreeRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        PendingDegreeDTO updated = pendingLecturerService.updatePendingDegree(request, user);
        return ResponseEntity.ok(updated);
    }
    @PostMapping("/resubmit-pending-lecturer")
    public ResponseEntity<PendingLecturerDTO> resubmitPendingLecturer(@RequestBody PendingLecturerDTO request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy người dùng đang đăng nhập");
        }
        PendingLecturerDTO updated = pendingLecturerService.resubmitPendingLecturer(request, user);
        return ResponseEntity.ok(updated);
    }








}
