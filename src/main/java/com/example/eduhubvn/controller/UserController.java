package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.FileResponse;
import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeReq;
import com.example.eduhubvn.dtos.lecturer.request.LecturerReq;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerOrganizationReq;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final GoogleDriveService googleDriveService;
    private final UserService userService;

    private final LecturerService lecturerService;
    private final EducationInstitutionService educationInstitutionService;
    private final PartnerOrganizationService partnerOrganizationService;

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "File is empty";
        } else {
            File tempFile = File.createTempFile("temp", file.getOriginalFilename());
            file.transferTo(tempFile);
            FileResponse fileResponse = googleDriveService.uploadFileToGoogleDrive(tempFile);
            System.out.println(fileResponse);
            return fileResponse;
        }
    }
    @GetMapping("/user-profile")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getUserProfile(@AuthenticationPrincipal User user) {
        UserProfileDTO dto = userService.getCurrentUserProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Lấy thành công thông tin người dùng hiện tại", dto));

    }

    @PostMapping("/register-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> createLecturerFromUser(@RequestBody LecturerReq req, @AuthenticationPrincipal User user) {
        LecturerDTO dto = lecturerService.createLecturerFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Tạo giảng viên thành công", dto));
    }
    @PostMapping("/register-eduins")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> createEduInsFromUser(@RequestBody EducationInstitutionReq req, @AuthenticationPrincipal User user) {
        EducationInstitutionDTO dto = educationInstitutionService.createEduInsFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Tạo Trung tâm đào tạo thành công", dto));
    }

    @PostMapping("/register-partner")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> createEduInsFromUser(@RequestBody PartnerOrganizationReq req, @AuthenticationPrincipal User user) {
        PartnerOrganizationDTO dto = partnerOrganizationService.createPartnerFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Tạo Đối tác thành công", dto));
    }

    @PostMapping("/add-degree")
    public ResponseEntity<ApiResponse<List<DegreeDTO>>> addDegree(@RequestBody List<DegreeReq> req, @AuthenticationPrincipal User user) {
        List<DegreeDTO> dto = lecturerService.saveDegrees(req, user);
        return ResponseEntity.ok(ApiResponse.success("Tạo Bằng cấp thành công", dto));
    }
    @PostMapping("/add-certification")
    public ResponseEntity<ApiResponse<List<CertificationDTO>>> addCertification(@RequestBody List<CertificationReq> req, @AuthenticationPrincipal User user) {
        List<CertificationDTO> dto = lecturerService.saveCertification(req, user);
        return ResponseEntity.ok(ApiResponse.success("Tạo Chứng chỉ thành công", dto));
    }
}
