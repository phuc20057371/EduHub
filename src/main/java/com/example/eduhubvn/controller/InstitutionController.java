package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.InstitutionProfileDTO;
import com.example.eduhubvn.dtos.edu.request.EduInsUpdateReq;
import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.EducationInstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/institution")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SCHOOL') or hasRole('ADMIN')")
public class InstitutionController {

    private final EducationInstitutionService educationInstitutionService;

    @GetMapping("/get-institution-profile")
    public ResponseEntity<ApiResponse<InstitutionProfileDTO>> getInstitutionProfile(@AuthenticationPrincipal User user) {
        InstitutionProfileDTO profile = educationInstitutionService.getInstitutionProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin trường thành công", profile));
    }
    

    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> updateEduInsFromUser(@RequestBody EduInsUpdateReq req, @AuthenticationPrincipal User user) {
        EducationInstitutionDTO updatedInstitution = educationInstitutionService.updateEduInsFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", updatedInstitution));
    }

    @GetMapping("/get-lecturers")
    public ResponseEntity<ApiResponse<List<LecturerInfoDTO>>> getLecturer( @AuthenticationPrincipal User user) {
        List<LecturerInfoDTO> lecturers = educationInstitutionService.getLecturers(user);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách giảng viên thành công", lecturers));
    }

}
