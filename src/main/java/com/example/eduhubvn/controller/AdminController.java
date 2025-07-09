package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.*;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @GetMapping("/lecturer-pending-updates")
    public ResponseEntity<ApiResponse<List<LecturerPendingDTO>>> getPendingLecturerUpdates() {
        List<LecturerPendingDTO> pendingList = lecturerService.getPendingLecturerUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }
    @GetMapping("/partner-pending-updates")
    public ResponseEntity<ApiResponse<List<PartnerOrganizationPendingDTO>>> getPendingPartnerUpdates() {
        List<PartnerOrganizationPendingDTO> pendingList = partnerOrganizationService.getPendingPartnerOrganizationUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }
    @GetMapping("/edu-pending-updates")
    public ResponseEntity<ApiResponse<List<EducationInstitutionPendingDTO>>> getPendingEduInstitutionUpdates() {
        List<EducationInstitutionPendingDTO> pendingList = educationInstitutionService.getPendingEducationInstitutionUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }
    @GetMapping("/pending-updates")
    public ResponseEntity<ApiResponse<AllPendingUpdateDTO>> getAllPendingUpdates() {
        AllPendingUpdateDTO allPending = adminService.getAllPendingUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách tất cả yêu cầu cập nhật chờ duyệt", allPending));
    }
    @GetMapping("/pending-application")
    public ResponseEntity<ApiResponse<AllPendingEntityDTO>> getAllPendingEntities() {
        AllPendingEntityDTO result = adminService.getAllPendingEntities();
        return ResponseEntity.ok(ApiResponse.success("Danh sách tất cả yêu cầu tạo mới chờ duyệt", result));
    }

/// Lecturer
    @PostMapping("/approve-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturer(@RequestBody IdRequest req) {
        LecturerDTO dto = adminService.approveLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Duyệt thành công", dto));
    }
    @PostMapping("/approve-lecturer-update")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturerUpdate(@RequestBody IdRequest req) {
        LecturerDTO dto = adminService.approveLecturerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }
    @PostMapping("/reject-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> rejectLecturer(@RequestBody RejectReq req) {
        LecturerDTO dto = adminService.rejectLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới", dto));
    }
    @PostMapping("/reject-lecturer-update")
    public ResponseEntity<ApiResponse<LecturerUpdateDTO>> rejectLecturerUpdate(@RequestBody RejectReq req) {
        LecturerUpdateDTO dto = adminService.rejectLecturerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

/// Education Institution
    @PostMapping("/approve-institution")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> approveEduIns(@RequestBody IdRequest req) {
        EducationInstitutionDTO dto = adminService.approveEduIns(req);
        return ResponseEntity.ok(ApiResponse.success("Duyệt thành công", dto));
    }
    @PostMapping("/approve-institution-update")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> approveEduInsUpdate(@RequestBody IdRequest req) {
        EducationInstitutionDTO dto = adminService.approveEduInsUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }
    @PostMapping("/reject-institution")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> rejectInstitution(@RequestBody RejectReq req) {
        EducationInstitutionDTO dto = adminService.rejectInstitution(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới", dto));
    }
    @PostMapping("/reject-institution-update")
    public ResponseEntity<ApiResponse<EducationInstitutionUpdateDTO>> rejectEduInsUpdate(@RequestBody RejectReq req) {
        EducationInstitutionUpdateDTO dto = adminService.rejectEduInsUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

/// Partner Organization
    @PostMapping("/approve-partner")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> approvePartner(@RequestBody IdRequest req) {
        PartnerOrganizationDTO dto = adminService.approvePartner(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }
    @PostMapping("/approve-partner-update")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> approvePartnerUpdate(@RequestBody IdRequest req) {
        PartnerOrganizationDTO dto = adminService.approvePartnerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }
    @PostMapping("/reject-partner")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> rejectPartner(@RequestBody RejectReq req) {
        PartnerOrganizationDTO dto = adminService.rejectPartner(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới", dto));
    }
    @PostMapping("/reject-partner-update")
    public ResponseEntity<ApiResponse<PartnerOrganizationUpdateDTO>> rejectPartnerUpdate(@RequestBody RejectReq req) {
        PartnerOrganizationUpdateDTO dto = adminService.rejectPartnerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }
/// Certification
    @PostMapping("/approve-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> approveCertification(@RequestBody IdRequest req) {
        CertificationDTO dto = adminService.approveCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }
    @PostMapping("/approve-certification-update")
    public ResponseEntity<ApiResponse<CertificationDTO>> approveCertificationUpdate(@RequestBody IdRequest req) {
        CertificationDTO dto = adminService.approveCertificationUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }
    @PostMapping("/reject-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> rejectCertification(@RequestBody RejectReq req) {
        CertificationDTO dto = adminService.rejectCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }
    @PostMapping("/reject-certification-edit")
    public ResponseEntity<ApiResponse<CertificationDTO>> rejectEditCertification(@RequestBody RejectReq req) {
        CertificationDTO dto = adminService.rejectEditCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

/// Degree
    @PostMapping("/approve-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> approveDegree(@RequestBody IdRequest req) {
        DegreeDTO dto = adminService.approveDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }
    @PostMapping("/approve-degree-update")
    public ResponseEntity<ApiResponse<DegreeDTO>> approveDegreeUpdate(@RequestBody IdRequest req) {
        DegreeDTO dto = adminService.approveDegreeUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }
    @PostMapping("/reject-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> rejectDegree(@RequestBody RejectReq req) {
        DegreeDTO dto = adminService.rejectDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }
    @PostMapping("/reject-degree-edit")
    public ResponseEntity<ApiResponse<DegreeDTO>> rejectDegreeUpdate(@RequestBody RejectReq req) {
        DegreeDTO dto = adminService.rejectDegreeUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

/// Attended Training Course
    @PostMapping("/approve-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> approveAttendedCourse(@RequestBody IdRequest req) {
        AttendedTrainingCourseDTO dto = adminService.approveAttendedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }
    @PostMapping("/approve-attended-course-update")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> approveAttendedCourseUpdate(@RequestBody IdRequest req) {
        AttendedTrainingCourseDTO dto = adminService.approveAttendedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }
    @PostMapping("/reject-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> rejectAttendedCourse(@RequestBody RejectReq req) {
        AttendedTrainingCourseDTO dto = adminService.rejectAttendedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }
    @PostMapping("/reject-attended-course-update")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> rejectAttendedCourseUpdate(@RequestBody RejectReq req) {
        AttendedTrainingCourseDTO dto = adminService.rejectAttendedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

/// Owned Training Course
    @PostMapping("/approve-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> approveOwnedCourse(@RequestBody IdRequest req) {
        OwnedTrainingCourseDTO dto = adminService.approveOwnedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }
    @PostMapping("/approve-owned-course-update")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> approveOwnedCourseUpdate(@RequestBody IdRequest req) {
        OwnedTrainingCourseDTO dto = adminService.approveOwnedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật hành công.", dto));
    }
    @PostMapping("/reject-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> rejectOwnedCourse(@RequestBody RejectReq req) {
        OwnedTrainingCourseDTO dto = adminService.rejectOwnedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }
    @PostMapping("/reject-owned-course-update")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> rejectOwnedCourseUpdate(@RequestBody RejectReq req) {
        OwnedTrainingCourseDTO dto = adminService.rejectOwnedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

/// Research Project
    @PostMapping("/approve-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> approveResearchProject(@RequestBody IdRequest req) {
    ResearchProjectDTO dto = adminService.approveResearchProject(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }
    @PostMapping("/approve-research-project-update")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> approveResearchProjectUpdate(@RequestBody IdRequest req) {
        ResearchProjectDTO dto = adminService.approveResearchProjectUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }
    @PostMapping("/reject-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> rejectResearchProject(@RequestBody RejectReq req) {
        ResearchProjectDTO dto = adminService.rejectResearchProject(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }
    @PostMapping("/reject-research-project-update")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> rejectResearchProjectUpdate(@RequestBody RejectReq req) {
        ResearchProjectDTO dto = adminService.rejectResearchProjectUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

}
