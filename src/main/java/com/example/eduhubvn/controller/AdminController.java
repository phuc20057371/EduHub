package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.*;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.*;
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
    private final AdminService adminService;


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
    @GetMapping("/lecturer-pending-updates")
    public ResponseEntity<ApiResponse<List<LecturerPendingDTO>>> getPendingLecturerUpdates() {
        List<LecturerPendingDTO> pendingList = lecturerService.getPendingLecturerUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu cập nhật đang chờ duyệt", pendingList));
    }
    @GetMapping("/partner-pending-updates")
    public ResponseEntity<ApiResponse<List<PartnerOrganizationPendingDTO>>> getPendingPartnerUpdates() {
        List<PartnerOrganizationPendingDTO> pendingList = partnerOrganizationService.getPendingPartnerOrganizationUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đối tác đang chờ duyệt", pendingList));
    }
    @GetMapping("/edu-pending-updates")
    public ResponseEntity<ApiResponse<List<EducationInstitutionPendingDTO>>> getPendingEduInstitutionUpdates() {
        List<EducationInstitutionPendingDTO> pendingList = educationInstitutionService.getPendingEducationInstitutionUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đơn vị giáo dục chờ duyệt", pendingList));
    }
    @GetMapping("/pending-updates")
    public ResponseEntity<ApiResponse<AllPendingUpdateDTO>> getAllPendingUpdates() {
        AllPendingUpdateDTO allPending = adminService.getAllPendingUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách tất cả yêu cầu chờ duyệt", allPending));
    }
    @GetMapping("/pending-application")
    public ResponseEntity<ApiResponse<AllPendingEntityDTO>> getAllPendingEntities() {
        AllPendingEntityDTO result = adminService.getAllPendingEntities();
        return ResponseEntity.ok(ApiResponse.success("Danh sách tất cả yêu cầu chờ duyệt", result));
    }
    @PostMapping("/lecturer-update/approve/{id}")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturerUpdate(@PathVariable Integer id) {
        LecturerDTO dto = adminService.approveLecturerUpdate(id);
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt cập nhật giảng viên thành công.", dto));
    }
    @PostMapping("/lecturer-update/reject")
    public ResponseEntity<ApiResponse<LecturerUpdateDTO>> rejectLecturerUpdate(@RequestBody RejectReq req) {
        LecturerUpdateDTO dto = adminService.rejectLecturerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }
    @PostMapping("/edu-ins-update/approve/{id}")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> approveEduInsUpdate(@PathVariable Integer id) {
        EducationInstitutionDTO dto = adminService.approveEduInsUpdate(id);
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt cập nhật trường thành công.", dto));
    }
    @PostMapping("/edu-ins-update/reject")
    public ResponseEntity<ApiResponse<EducationInstitutionUpdateDTO>> rejectEduInsUpdate(@RequestBody RejectReq req) {
        EducationInstitutionUpdateDTO dto = adminService.rejectEduInsUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }
    @PostMapping("/partner-update/approve/{id}")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> approvePartnerUpdate(@PathVariable Integer id) {
        PartnerOrganizationDTO dto = adminService.approvePartnerUpdate(id);
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt cập nhật trường thành công.", dto));
    }
    @PostMapping("/partner-update/reject")
    public ResponseEntity<ApiResponse<PartnerOrganizationUpdateDTO>> rejectPartnerUpdate(@RequestBody RejectReq req) {
        PartnerOrganizationUpdateDTO dto = adminService.rejectPartnerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/certification/reject")
    public ResponseEntity<ApiResponse<CertificationDTO>> rejectCertification(@RequestBody RejectReq req) {
        CertificationDTO dto = lecturerService.rejectCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối chứng chỉ thành công.", dto));
    }
    @PostMapping("/degree/reject")
    public ResponseEntity<ApiResponse<DegreeDTO>> rejectDegree(@RequestBody RejectReq req) {
        DegreeDTO dto = lecturerService.rejectDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối chứng chỉ thành công.", dto));
    }
    @PostMapping("/degree-edit/reject")
    public ResponseEntity<ApiResponse<DegreeDTO>> rejectEditDegree(@RequestBody RejectReq req) {
        DegreeDTO dto = lecturerService.rejectEditDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối chỉnh sửa bằng cấp thành công.", dto));
    }
    @PostMapping("/certification-edit/reject")
    public ResponseEntity<ApiResponse<CertificationDTO>> rejectEditCertification(@RequestBody RejectReq req) {
        CertificationDTO dto = lecturerService.rejectEditCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối chỉnh sửa chứng chỉ thành công.", dto));
    }













}
