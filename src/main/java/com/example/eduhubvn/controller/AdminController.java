package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.EducationInstitutionService;
import com.example.eduhubvn.services.LecturerService;
import com.example.eduhubvn.services.PartnerOrganizationService;
import com.example.eduhubvn.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final LecturerService lecturerService;
    private final EducationInstitutionService educationInstitutionService;
    private final PartnerOrganizationService partnerOrganizationService;


    @PostMapping("/approve-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturer(@RequestBody IdRequest req) {
        LecturerDTO dto = lecturerService.approveLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }
    @PostMapping("/approve-institution")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> approveEduIns(@RequestBody IdRequest req) {
        EducationInstitutionDTO dto = educationInstitutionService.approveEduIns(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }
    @PostMapping("/approve-partner")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> approvePartner(@RequestBody IdRequest req) {
        PartnerOrganizationDTO dto = partnerOrganizationService.approvePartner(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }
    @PostMapping("/approve-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> approveDegree(@RequestBody IdRequest req) {
        DegreeDTO dto = lecturerService.approveDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }
    @PostMapping("/approve-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> approveCertification(@RequestBody IdRequest req) {
        CertificationDTO dto = lecturerService.approveCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }




}
