package com.example.eduhubvn.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.IdReq;
import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.dtos.auth.Email;
import com.example.eduhubvn.dtos.auth.EmailOtp;
import com.example.eduhubvn.dtos.auth.request.ChangePasswordReq;
import com.example.eduhubvn.dtos.institution.InstitutionDTO;
import com.example.eduhubvn.dtos.institution.request.InstitutionCreateReq;
import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerAllInfoDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.PendingLecturerDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.CertificationUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeUpdateReq;
import com.example.eduhubvn.dtos.lecturer.request.LecturerCreateReq;
import com.example.eduhubvn.dtos.partner.PartnerDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerCreateReq;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.AdminService;
import com.example.eduhubvn.services.AuthenticationService;
import com.example.eduhubvn.services.InstitutionService;
import com.example.eduhubvn.services.LecturerService;
import com.example.eduhubvn.services.PartnerService;
import com.example.eduhubvn.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LecturerService lecturerService;
    private final InstitutionService educationInstitutionService;
    private final PartnerService partnerOrganizationService;
    private final AdminService adminService;
    private final AuthenticationService authenticationService;

    @PostMapping("/uploads")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        User user = (User) authentication.getPrincipal();
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (file.isEmpty() || (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".jpeg") &&
                !originalFileName.endsWith(".png") && !originalFileName.endsWith(".pdf")
                && !originalFileName.endsWith(".doc") && !originalFileName.endsWith(".docx"))) {
            return ResponseEntity.badRequest().body("File must be .jpg, .jpeg, .png or .pdf");
        }

        try {
            // Lấy phần tên và phần đuôi file
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."))
                    .replaceAll("[^a-zA-Z0-9_-]", "_"); // tránh ký tự đặc biệt

            // Tạo tên file mới có timestamp
            String newFileName = baseName + "_" + System.currentTimeMillis() + fileExtension;

            // Đường dẫn upload
            Path uploadPath = Paths.get("uploads", String.valueOf(user.getRole()), String.valueOf(user.getId()));
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Tạo URL truy cập
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String fileUrl = baseUrl + "/uploads/" + user.getRole() + "/" + user.getId() + "/" + newFileName;

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
    public ResponseEntity<ApiResponse<LecturerDTO>> createLecturer(@RequestBody LecturerCreateReq req,
            @AuthenticationPrincipal User user) {
        LecturerDTO dto = lecturerService.createLecturer(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/update-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> updateLecturer(@RequestBody LecturerCreateReq req,
            @AuthenticationPrincipal User user) {
        LecturerDTO request = lecturerService.updateLecturer(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", request));
    }

    @GetMapping("/pending-lecturer-profile")
    public ResponseEntity<ApiResponse<PendingLecturerDTO>> getPendingLecturerProfile(
            @AuthenticationPrincipal User user) {
        PendingLecturerDTO pending = lecturerService.getPendingLecturerProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Danh sách hồ sơ đang chờ duyệt", pending));
    }

    @PostMapping("/resubmit-lecturer")
    public ResponseEntity<ApiResponse<PendingLecturerDTO>> updatePendingLecturer(@RequestBody PendingLecturerDTO req,
            @AuthenticationPrincipal User user) {
        PendingLecturerDTO pending = lecturerService.updatePendingLecturer(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", pending));
    }

    @GetMapping("/lecturer-profile/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SCHOOL') or hasRole('ORGANIZATION') or hasRole('LECTURER') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:read'))")
    public ResponseEntity<ApiResponse<LecturerAllInfoDTO>> getLecturerProfile(@PathVariable UUID lecturerId,
            @AuthenticationPrincipal User user) {
        LecturerAllInfoDTO lecturer = adminService.getLecturerProfile(lecturerId, user);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin giảng viên thành công", lecturer));
    }

    /// Degree
    @PostMapping("/create-degree")
    public ResponseEntity<ApiResponse<List<DegreeDTO>>> addDegree(@RequestBody List<DegreeCreateReq> req,
            @AuthenticationPrincipal User user) {
        List<DegreeDTO> dto = lecturerService.saveDegrees(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/update-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> updateDegree(@RequestBody DegreeUpdateReq req,
            @AuthenticationPrincipal User user) {
        DegreeDTO dto = lecturerService.updateDegree(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/edit-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> editDegree(@RequestBody DegreeUpdateReq req,
            @AuthenticationPrincipal User user) {
        DegreeDTO dto = lecturerService.editDegree(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/delete-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> deleteDegree(@RequestBody IdReq req) {
        DegreeDTO dto = lecturerService.deleteDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công.", dto));
    }

    /// Certification
    @PostMapping("/create-certification")
    public ResponseEntity<ApiResponse<List<CertificationDTO>>> addCertification(@RequestBody List<CertificationCreateReq> req,
            @AuthenticationPrincipal User user) {
        List<CertificationDTO> dto = lecturerService.saveCertification(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/update-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> updateCertification(@RequestBody CertificationUpdateReq req,
            @AuthenticationPrincipal User user) {
        CertificationDTO dto = lecturerService.updateCertification(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/edit-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> editCertificationFromUser(
            @RequestBody CertificationUpdateReq req, @AuthenticationPrincipal User user) {
        CertificationDTO dto = lecturerService.editCertificationFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @PostMapping("/delete-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> deleteCertification(@RequestBody IdReq req) {
        CertificationDTO dto = lecturerService.deleteCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công.", dto));
    }

    /// Education Institution
    @PostMapping("/register-institution")
    public ResponseEntity<ApiResponse<InstitutionDTO>> createEduInsFromUser(
            @RequestBody InstitutionCreateReq req, @AuthenticationPrincipal User user) {
        InstitutionDTO dto = educationInstitutionService.createEduInsFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/update-institution")
    public ResponseEntity<ApiResponse<InstitutionDTO>> updateEduins(@RequestBody InstitutionCreateReq req,
            @AuthenticationPrincipal User user) {
        InstitutionDTO dto = educationInstitutionService.updateEduins(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @GetMapping("/pending-institution-profile")
    public ResponseEntity<ApiResponse<InstitutionDTO>> getPendingEduinsProfile(
            @AuthenticationPrincipal User user) {
        InstitutionDTO pending = educationInstitutionService.getPendingEduinsProfile(user);
        return ResponseEntity.ok(ApiResponse.success("lấy hồ sơ thành công", pending));
    }

    /// Partner Organization
    @PostMapping("/register-partner")
    public ResponseEntity<ApiResponse<PartnerDTO>> createEduInsFromUser(
            @RequestBody PartnerCreateReq req, @AuthenticationPrincipal User user) {
        PartnerDTO dto = partnerOrganizationService.createPartnerFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/update-partner")
    public ResponseEntity<ApiResponse<PartnerDTO>> updatePartner(@RequestBody PartnerCreateReq req,
            @AuthenticationPrincipal User user) {
        PartnerDTO dto = partnerOrganizationService.updatePartner(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", dto));
    }

    @GetMapping("/pending-partner-profile")
    public ResponseEntity<ApiResponse<PartnerDTO>> getPendingPartnerProfile(
            @AuthenticationPrincipal User user) {
        PartnerDTO pending = partnerOrganizationService.getPendingPartnerProfile(user);
        return ResponseEntity.ok(ApiResponse.success("lấy hồ sơ thành công", pending));
    }

    @PostMapping("/send-otp-change-password")
    public ResponseEntity<ApiResponse<String>> sendOtpChangePassword(@RequestBody Email request,
            @AuthenticationPrincipal User user) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Request không được để trống", null));
            }

            String message = authenticationService.sendOTPChangePassword(request, user);
            return ResponseEntity.ok(ApiResponse.success(message, null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Có lỗi xảy ra khi gửi OTP", null));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordReq request,
            @AuthenticationPrincipal User user) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Request không được để trống", null));
            }

            String message = authenticationService.changePassword(request, user);
            return ResponseEntity.ok(ApiResponse.success(message, null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Có lỗi xảy ra khi thay đổi mật khẩu", null));
        }
    }
    

    @PostMapping("/send-otp-add-subemail")
    public ResponseEntity<ApiResponse<String>> sendOtpAddSubEmail(@RequestBody Email request,
            @AuthenticationPrincipal User user) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Request không được để trống", null));
            }

            String message = authenticationService.sendOTPAddSubEmail(request, user);
            return ResponseEntity.ok(ApiResponse.success(message, null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Có lỗi xảy ra khi gửi OTP", null));
        }
    }

    @PostMapping("/add-subemail")
    public ResponseEntity<ApiResponse<String>> addSubEmail(@RequestBody EmailOtp request,
            @AuthenticationPrincipal User user) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Request không được để trống", null));
            }

            String message = authenticationService.addSubEmail(request, user);
            return ResponseEntity.ok(ApiResponse.success(message, null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Có lỗi xảy ra khi thêm email", null));
        }
    }

    @PostMapping("/remove-subemail")
    public ResponseEntity<ApiResponse<String>> removeSubEmail(@RequestBody Email request,
            @AuthenticationPrincipal User user) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Request không được để trống", null));
            }

            String message = authenticationService.removeSubEmail(request, user);
            return ResponseEntity.ok(ApiResponse.success(message, null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Có lỗi xảy ra khi xóa email", null));
        }
    }

    @GetMapping("/subemail-list")
    public ResponseEntity<ApiResponse<Set<String>>> getSubEmailList(@AuthenticationPrincipal User user) {
        try {
            Set<String> subEmails = authenticationService.getSubEmails(user);
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sub-email thành công", subEmails));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Có lỗi xảy ra khi lấy danh sách email", null));
        }
    }

}
