package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.edu.request.EduInsUpdateReq;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.EducationInstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/edu-ins")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SCHOOL') or hasRole('ADMIN')")
public class EduInsController {

    private final EducationInstitutionService educationInstitutionService;

    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse<EduInsUpdateReq>> updateEduInsFromUser(@RequestBody EduInsUpdateReq req, @AuthenticationPrincipal User user) {
        EduInsUpdateReq request = educationInstitutionService.updateEduInsFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", request));
    }

}
