package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.FileResponse;
import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.LecturerReq;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerOrganizationReq;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final GoogleDriveService googleDriveService;
    private final UserService userService;
    private final LecturerService lecturerService;
    private final EducationInstitutionService educationInstitutionService;
    private final PartnerOrganizationService partnerOrganizationService;
    private final AdminService adminService;


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
    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/uploads")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        HttpServletRequest request) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty() || (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") &&
                !fileName.endsWith(".png") && !fileName.endsWith(".pdf"))) {
            return ResponseEntity.badRequest().body("File must be .jpg, .jpeg, .png or .pdf");
        }

        try {
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Build file access URL
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String fileUrl = baseUrl + "/uploads/" + fileName;

            return ResponseEntity.ok().body(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
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


    @GetMapping("/find-lecturers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SCHOOL') or hasRole('OGANIZATION')")
    public ResponseEntity<ApiResponse<List<LecturerDTO>>> findLecturers(@RequestParam String academicRank, @RequestParam String specialization) {
        List<LecturerDTO> lecturers = userService.findLecturers(academicRank, specialization);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách giảng viên thành công", lecturers));
    }

    @GetMapping("/lecturer-profile/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SCHOOL') or hasRole('OGANIZATION')")
    public ResponseEntity<ApiResponse<LecturerAllInfoDTO>> getLecturerProfile(@PathVariable Integer lecturerId) {
        LecturerAllInfoDTO lecturer = adminService.getLecturerProfile(lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin giảng viên thành công", lecturer));
    }

}
