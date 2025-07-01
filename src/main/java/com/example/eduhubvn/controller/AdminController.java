package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.admin.AllPendingApplicationsDTO;
import com.example.eduhubvn.dtos.admin.ApplicationPendingReject;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionReq;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.ApproveRequest;
import com.example.eduhubvn.dtos.lecturer.request.RejectPendingLecturerRequest;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationReq;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.repositories.PendingEducationInstitutionRepository;
import com.example.eduhubvn.repositories.PendingLecturerRepository;
import com.example.eduhubvn.repositories.PendingPartnerOrganizationRepository;
import com.example.eduhubvn.services.EducationInstitutionService;
import com.example.eduhubvn.services.LecturerService;
import com.example.eduhubvn.services.PartnerOrganizationService;
import com.example.eduhubvn.services.PendingLecturerService;
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
    private final PendingLecturerRepository pendingLecturerRepository;
    private final PendingEducationInstitutionRepository pendingEduRepo;
    private final PendingPartnerOrganizationRepository pendingPartnerRepo;

    private final EducationInstitutionService educationInstitutionService;
    private final PartnerOrganizationService partnerOrganizationService;
    private final LecturerService lecturerService;
    private final PendingLecturerService pendingLecturerService;

    @GetMapping("/all-pending")
    public ResponseEntity<ApiResponse<AllPendingApplicationsDTO>> getAllPendingApplications() {
        List<PendingLecturer> lecturers = pendingLecturerRepository.findByStatus(PendingStatus.PENDING);
        List<PendingEducationInstitution> institutions = pendingEduRepo.findByStatus(PendingStatus.PENDING);
        List<PendingPartnerOrganization> partners = pendingPartnerRepo.findByStatus(PendingStatus.PENDING);

        AllPendingApplicationsDTO result = new AllPendingApplicationsDTO();
        result.setLecturers(lecturers.stream().map(LecturerMapper::toPendingLecturerDTO).toList());
        result.setEducationInstitutions(institutions.stream().map(EducationInstitutionMapper::toDTO).toList());
        result.setPartnerOrganizations(partners.stream().map(PartnerOrganizationMapper::toPendingDTO).toList());

        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đơn đăng ký thành công", result));
    }

    @PostMapping("/approve-edu-ins")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> approveEduInstitution(@RequestBody EducationInstitutionReq req) {
        EducationInstitutionDTO approved = educationInstitutionService.approvePendingInstitution(req);
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt cơ sở giáo dục thành công", approved));
    }

    @PostMapping("/approve-p-org")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> approvePartnerOrganization(@RequestBody PartnerOrganizationReq req) {
        PartnerOrganizationDTO approved = partnerOrganizationService.approvePartnerOrganization(req);
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt tổ chức đối tác thành công", approved));
    }

    @PostMapping("/approve-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturer(@RequestBody LecturerReq req) {
        LecturerDTO lecturer = lecturerService.approveLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt giảng viên thành công", lecturer));
    }

    @PostMapping("/reject-pending-lecturer")
    public ResponseEntity<ApiResponse<String>> rejectPendingLecturer(@RequestBody RejectPendingLecturerRequest request) {
        lecturerService.rejectPendingLecturer(request);
        return ResponseEntity.ok(ApiResponse.success("Từ chối hồ sơ đăng ký giảng viên thành công", request.getResponse()));
    }

    @PostMapping("/approve-lecturer-profile")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturerProfile(@RequestBody LecturerReq req) {
        LecturerDTO lecturer = lecturerService.approvePendingLecturerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt cập nhật hồ sơ giảng viên thành công", lecturer));
    }

    @PostMapping("/reject-lecturer-update")
    public ResponseEntity<ApiResponse<String>> rejectPendingLecturerUpdate(@RequestBody ApplicationPendingReject req) {
        lecturerService.rejectPendingLecturerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật hồ sơ giảng viên: " + req.getId(), req.getReason()));
    }

    @PostMapping("/approve-pending-certification")
    public ResponseEntity<ApiResponse<CertificationDTO>> approvePendingCertification(@RequestBody ApproveRequest req) {
        CertificationDTO result = lecturerService.approvePendingCertification(req.getId());
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt chứng chỉ thành công", result));
    }

    @PostMapping("/approve-pending-degree")
    public ResponseEntity<ApiResponse<DegreeDTO>> approvePendingDegree(@RequestBody ApproveRequest req) {
        DegreeDTO result = lecturerService.approvePendingDegree(req.getId());
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt bằng cấp thành công", result));
    }

    @PostMapping("/reject-certification")
    public ResponseEntity<ApiResponse<String>> rejectCertification(@RequestBody ApplicationPendingReject req) {
        lecturerService.rejectPendingCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối chứng chỉ " + req.getId(), req.getReason()));
    }

    @PostMapping("/reject-degree")
    public ResponseEntity<ApiResponse<String>> rejectDegree(@RequestBody ApplicationPendingReject req) {
        lecturerService.rejectPendingDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối bằng cấp " + req.getId(), req.getReason()));
    }

    @PostMapping("/approve-owned-course")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> approveOwnedCourse(@RequestBody ApproveRequest req) {
        OwnedTrainingCourseDTO result = lecturerService.approveOwnedCourse(req.getId());
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt khóa đạo tạo thành công", result));
    }

    @PostMapping("/approve-attended-course")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> approveAttendedCourse(@RequestBody ApproveRequest req) {
        AttendedTrainingCourseDTO result = lecturerService.approveAttendedCourse(req.getId());
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt khóa đạo tạo thành công", result));
    }
    @PostMapping("/approve-research-project")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> approveResearchProject(@RequestBody ApproveRequest req) {
        ResearchProjectDTO result = lecturerService.approveResearchProject(req.getId());
        return ResponseEntity.ok(ApiResponse.success("Phê duyệt khóa đạo tạo thành công", result));
    }

    @PostMapping("/reject-owned-course")
    public ResponseEntity<ApiResponse<String>> rejectOwnedCourse(@RequestBody ApplicationPendingReject req) {
        lecturerService.rejectOwnedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối khóa đào tạo " + req.getId(), req.getReason()));
    }






}
