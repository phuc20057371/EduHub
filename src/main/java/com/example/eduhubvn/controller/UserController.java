package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.FileResponse;
import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.PendingLecturerDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeUpdateReq;
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
        return ResponseEntity.ok(ApiResponse.success("Thông tin người dùng hiện tại", dto));
    }
/// Lecturer
    @PostMapping("/register-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> createLecturer(@RequestBody LecturerReq req, @AuthenticationPrincipal User user) {
        LecturerDTO dto = lecturerService.createLecturer(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> updateLecturer(@RequestBody LecturerReq req, @AuthenticationPrincipal User user) {
        LecturerDTO request = lecturerService.updateLecturer(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", request));
    }
    @GetMapping("/pending-lecturer-profile")
    public ResponseEntity<ApiResponse<PendingLecturerDTO>> getPendingLecturerProfile(@AuthenticationPrincipal User user) {
        PendingLecturerDTO pending = lecturerService.getPendingLecturerProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Danh sách hồ sơ đang chờ duyệt", pending));
    }
    @PostMapping("/resubmit-lecturer")
    public ResponseEntity<ApiResponse<PendingLecturerDTO>> updatePendingLecturer(@RequestBody PendingLecturerDTO req, @AuthenticationPrincipal User user) {
        PendingLecturerDTO pending = lecturerService.updatePendingLecturer(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", pending));
    }
/// Degree
    @PostMapping("/create-degree")
    public ResponseEntity<ApiResponse<List<DegreeDTO>>> addDegree(@RequestBody List<DegreeReq> req, @AuthenticationPrincipal User user) {
        List<DegreeDTO> dto = lecturerService.saveDegrees(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> updateDegree(@RequestBody DegreeUpdateReq req, @AuthenticationPrincipal User user) {
        DegreeDTO dto = lecturerService.updateDegree(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
/// Certification
    @PostMapping("/create-certification")
    public ResponseEntity<ApiResponse<List<CertificationDTO>>> addCertification(@RequestBody List<CertificationReq> req, @AuthenticationPrincipal User user) {
        List<CertificationDTO> dto = lecturerService.saveCertification(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
/// Education Institution
    @PostMapping("/register-institution")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> createEduInsFromUser(@RequestBody EducationInstitutionReq req, @AuthenticationPrincipal User user) {
        EducationInstitutionDTO dto = educationInstitutionService.createEduInsFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-institution")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> updateEduins(@RequestBody EducationInstitutionReq req, @AuthenticationPrincipal User user) {
        EducationInstitutionDTO dto = educationInstitutionService.updateEduins(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }
    @GetMapping("/pending-institution-profile")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> getPendingEduinsProfile(@AuthenticationPrincipal User user) {
        EducationInstitutionDTO pending = educationInstitutionService.getPendingEduinsProfile(user);
        return ResponseEntity.ok(ApiResponse.success("lấy hồ sơ thành công", pending));
    }
/// Partner Organization
    @PostMapping("/register-partner")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> createEduInsFromUser(@RequestBody PartnerOrganizationReq req, @AuthenticationPrincipal User user) {
        PartnerOrganizationDTO dto = partnerOrganizationService.createPartnerFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }
    @PostMapping("/update-partner")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> updatePartner(@RequestBody PartnerOrganizationReq req, @AuthenticationPrincipal User user) {
        PartnerOrganizationDTO dto = partnerOrganizationService.updatePartner(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @GetMapping("/pending-partner-profile")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> getPendingPartnerProfile(@AuthenticationPrincipal User user) {
        PartnerOrganizationDTO pending = partnerOrganizationService.getPendingPartnerProfile(user);
        return ResponseEntity.ok(ApiResponse.success("lấy hồ sơ thành công", pending));
    }
}
