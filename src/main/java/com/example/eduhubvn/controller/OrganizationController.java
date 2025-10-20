package com.example.eduhubvn.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerProfileDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerUpdateReq;
import com.example.eduhubvn.dtos.program.TrainingProgramRequestDTO;
import com.example.eduhubvn.dtos.program.TrainingProgramRequestReq;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.PartnerOrganizationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/partner")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
public class OrganizationController {

    private final PartnerOrganizationService partnerOrganizationService;
    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> updatePartnerFromUser(@RequestBody PartnerUpdateReq req, @AuthenticationPrincipal User user) {
        PartnerOrganizationDTO request = partnerOrganizationService.updatePartnerFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", request));
    }

    @GetMapping("/get-partner-profile")
    public ResponseEntity<ApiResponse<PartnerProfileDTO>> getPartnerProfile(@AuthenticationPrincipal User user) {
        PartnerProfileDTO partnerProfile = partnerOrganizationService.getPartnerProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin đối tác thành công", partnerProfile));
    } 

    /// Logo
    @PostMapping("/update-logo")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> updatePartnerLogo(@RequestParam("file") MultipartFile file,
            HttpServletRequest request, @AuthenticationPrincipal User user) {
        
        // Validate file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File không được trống", null));
        }
        
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File phải có định dạng .jpg, .jpeg hoặc .png", null));
        }

        try {
            // Create upload directory
            Path uploadPath = Paths.get("uploads", String.valueOf(user.getRole()), String.valueOf(user.getId()));
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Build file access URL
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String fileUrl = baseUrl + "/uploads/" + user.getRole() + "/" + user.getId() + "/" + fileName;

            // Update partner logo
            PartnerOrganizationDTO dto = partnerOrganizationService.updatePartnerLogo(fileUrl, user);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật logo thành công", dto));
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi upload file: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi cập nhật logo: " + e.getMessage(), null));
        }
    }

    @GetMapping("/get-all-training-program-requests")
    public ResponseEntity<ApiResponse<List<TrainingProgramRequestDTO>>> getAllTrainingProgramRequests(@AuthenticationPrincipal User user) {
        List<TrainingProgramRequestDTO> requests = partnerOrganizationService.getAllTrainingProgramRequests(user);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách yêu cầu chương trình đào tạo thành công", requests));
    }
    @PostMapping("/create-training-program-request")
    public ResponseEntity<ApiResponse<TrainingProgramRequestDTO>> createTrainingProgramRequest(@RequestBody TrainingProgramRequestReq request,
            @AuthenticationPrincipal User user) {
        TrainingProgramRequestDTO createdRequest = partnerOrganizationService.createTrainingProgramRequest(request, user);
        return ResponseEntity.ok(ApiResponse.success("Tạo yêu cầu chương trình đào tạo thành công", createdRequest));
    }

}
