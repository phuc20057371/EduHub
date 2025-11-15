package com.example.eduhubvn.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.IdReq;
import com.example.eduhubvn.dtos.PaginatedResponse;
import com.example.eduhubvn.dtos.RejectReq;
import com.example.eduhubvn.dtos.RequestFromLecturer;
import com.example.eduhubvn.dtos.admin.request.CreateInstitutionReq;
import com.example.eduhubvn.dtos.admin.request.CreateLecturerReq;
import com.example.eduhubvn.dtos.admin.request.CreatePartnerReq;
import com.example.eduhubvn.dtos.institution.InstitutionDTO;
import com.example.eduhubvn.dtos.institution.InstitutionPendingDTO;
import com.example.eduhubvn.dtos.institution.InstitutionUpdateDTO;
import com.example.eduhubvn.dtos.institution.InstitutionInfoDTO;
import com.example.eduhubvn.dtos.lecturer.AttendedCourseDTO;
import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerAllProfileDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerCreateDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerPendingDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerUpdateDTO;

import com.example.eduhubvn.dtos.lecturer.OwnedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.ResearchProjectDTO;
import com.example.eduhubvn.dtos.lecturer.request.AttendedCourseCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.CertificationCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedCourseCreateReq;
import com.example.eduhubvn.dtos.lecturer.request.ResearchProjectCreateReq;
import com.example.eduhubvn.dtos.partner.PartnerInfoDTO;
import com.example.eduhubvn.dtos.partner.PartnerDTO;
import com.example.eduhubvn.dtos.partner.PartnerPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerUpdateDTO;
import com.example.eduhubvn.dtos.program.TrainingProgramDTO;
import com.example.eduhubvn.dtos.program.TrainingProgramRequestDTO;
import com.example.eduhubvn.dtos.program.TrainingUnitDTO;
import com.example.eduhubvn.dtos.program.request.TrainingProgramReq;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.AdminService;
import com.example.eduhubvn.services.InstitutionService;
import com.example.eduhubvn.services.LecturerService;
import com.example.eduhubvn.services.PartnerService;
import com.example.eduhubvn.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('SUB_ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final LecturerService lecturerService;
    private final InstitutionService educationInstitutionService;
    private final PartnerService partnerOrganizationService;

    /// General


    /// Lecturer

    @GetMapping("/get-degree-requests")
    public ResponseEntity<ApiResponse<List<RequestFromLecturer<?>>>> getDegreeRequests() {
        List<RequestFromLecturer<?>> requests = adminService.getDegreeRequests();
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu bằng cấp từ giảng viên", requests));
    }

    @GetMapping("/get-certification-requests")
    public ResponseEntity<ApiResponse<List<RequestFromLecturer<?>>>> getCertificationRequests() {
        List<RequestFromLecturer<?>> requests = adminService.getCertificationRequests();
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu chứng chỉ từ giảng viên", requests));
    }

    @GetMapping("/get-attended-course-requests")
    public ResponseEntity<ApiResponse<List<RequestFromLecturer<?>>>> getAttendedCourseRequests() {
        List<RequestFromLecturer<?>> requests = adminService.getAttendedCourseRequests();
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu khóa học đã tham gia từ giảng viên", requests));
    }

    @GetMapping("/get-owned-course-requests")
    public ResponseEntity<ApiResponse<List<RequestFromLecturer<?>>>> getOwnedCourseRequests() {
        List<RequestFromLecturer<?>> requests = adminService.getOwnedCourseRequests();
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu khóa học sở hữu từ giảng viên", requests));
    }

    @GetMapping("/get-research-project-requests")
    public ResponseEntity<ApiResponse<List<RequestFromLecturer<?>>>> getResearchProjectRequests() {
        List<RequestFromLecturer<?>> requests = adminService.getResearchProjectRequests();
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu dự án nghiên cứu từ giảng viên", requests));
    }

    @GetMapping("/get-all-lecturers")
    public ResponseEntity<ApiResponse<List<LecturerInfoDTO>>> getAllLecturers() {
        List<LecturerInfoDTO> lecturers = adminService.getAllLecturers();
        return ResponseEntity.ok(ApiResponse.success("Danh sách giảng viên", lecturers));
    }

    @GetMapping("/get-all-lecturers-paginated")
    public ResponseEntity<ApiResponse<PaginatedResponse<LecturerInfoDTO>>> getAllLecturersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginatedResponse<LecturerInfoDTO> lecturers = adminService.getAllLecturersPaginated(page, size);
        return ResponseEntity.ok(ApiResponse.success("Danh sách giảng viên có phân trang", lecturers));
    }

    @GetMapping("/lecturer-pending-updates")
    public ResponseEntity<ApiResponse<List<LecturerPendingDTO>>> getPendingLecturerUpdates() {
        List<LecturerPendingDTO> pendingList = lecturerService.getPendingLecturerUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @GetMapping("/lecturer-pending-create")
    public ResponseEntity<ApiResponse<List<LecturerCreateDTO>>> getPendingLecturerCreate() {
        List<LecturerCreateDTO> pendingList = lecturerService.getPendingLecturerCreate();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @PostMapping("/approve-lecturer")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturer(@RequestBody IdReq req) {
        LecturerDTO dto = adminService.approveLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Duyệt thành công", dto));
    }

    @PostMapping("/approve-lecturer-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturerUpdate(@RequestBody IdReq req) {
        LecturerDTO dto = adminService.approveLecturerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-lecturer")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<LecturerDTO>> rejectLecturer(@RequestBody RejectReq req) {
        LecturerDTO dto = adminService.rejectLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới", dto));
    }

    @PostMapping("/reject-lecturer-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<LecturerUpdateDTO>> rejectLecturerUpdate(@RequestBody RejectReq req) {
        LecturerUpdateDTO dto = adminService.rejectLecturerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/update-lecturer")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:update'))")
    public ResponseEntity<ApiResponse<LecturerDTO>> updateLecturer(@RequestBody LecturerUpdateDTO req) {
        LecturerDTO dto = adminService.updateLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @GetMapping("/get-lecturer-all-profile/{id}")
    public ResponseEntity<ApiResponse<LecturerAllProfileDTO>> getLecturerProfile(@PathVariable UUID id) {
        LecturerAllProfileDTO profile = adminService.getLecturerAllProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Thông tin giảng viên", profile));
    }

    @PostMapping("/delete-lecturer")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteLecturer(@RequestBody IdReq req) {
        try {
            adminService.deleteLecturer(req);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }

    @PostMapping("/create-lecturer")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:create'))")
    public ResponseEntity<ApiResponse<LecturerDTO>> createLecturer(@RequestBody CreateLecturerReq req,
            @AuthenticationPrincipal User user) {
        LecturerDTO dto = adminService.createLecturer(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    /// Education Institution

    @GetMapping("/get-all-institutions")
    public ResponseEntity<ApiResponse<List<InstitutionDTO>>> getAllInstitutions() {
        List<InstitutionDTO> institutions = adminService.getAllInstitutions();
        return ResponseEntity.ok(ApiResponse.success("Danh sách cơ sở giáo dục", institutions));
    }

    @GetMapping("/institution-pending-updates")
    public ResponseEntity<ApiResponse<List<InstitutionPendingDTO>>> getPendingEduInstitutionUpdates() {
        List<InstitutionPendingDTO> pendingList = educationInstitutionService
                .getPendingEducationInstitutionUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @GetMapping("/institution-pending-create")
    public ResponseEntity<ApiResponse<List<InstitutionInfoDTO>>> getPendingEduInstitutionCreate() {
        List<InstitutionInfoDTO> pendingList = educationInstitutionService.getPendingEducationInstitutionCreate();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @PostMapping("/approve-institution")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:approve'))")
    public ResponseEntity<ApiResponse<InstitutionDTO>> approveEduIns(@RequestBody IdReq req) {
        InstitutionDTO dto = adminService.approveEduIns(req);
        return ResponseEntity.ok(ApiResponse.success("Duyệt thành công", dto));
    }

    @PostMapping("/approve-institution-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:approve'))")
    public ResponseEntity<ApiResponse<InstitutionDTO>> approveEduInsUpdate(@RequestBody IdReq req) {
        InstitutionDTO dto = adminService.approveEduInsUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-institution")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:approve'))")
    public ResponseEntity<ApiResponse<InstitutionDTO>> rejectInstitution(@RequestBody RejectReq req) {
        InstitutionDTO dto = adminService.rejectInstitution(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới", dto));
    }

    @PostMapping("/reject-institution-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:approve'))")
    public ResponseEntity<ApiResponse<InstitutionUpdateDTO>> rejectEduInsUpdate(@RequestBody RejectReq req) {
        InstitutionUpdateDTO dto = adminService.rejectEduInsUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/update-institution")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:update'))")
    public ResponseEntity<ApiResponse<InstitutionDTO>> updateInstitution(
            @RequestBody InstitutionUpdateDTO req) {
        InstitutionDTO dto = adminService.updateInstitution(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:delete'))")
    @PostMapping("/delete-institution")
    public ResponseEntity<ApiResponse<Void>> deleteInstitution(@RequestBody IdReq id) {
        try {
            adminService.deleteInstitution(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }

    @PostMapping("/create-institution")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:create'))")
    public ResponseEntity<ApiResponse<InstitutionDTO>> createInstitution(
            @RequestBody CreateInstitutionReq req,
            @AuthenticationPrincipal User user) {
        InstitutionDTO dto = adminService.createInstitution(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    /// Partner Organization

    @GetMapping("/get-all-partners")
    public ResponseEntity<ApiResponse<List<PartnerDTO>>> getAllPartners() {
        List<PartnerDTO> partners = adminService.getAllPartners();
        return ResponseEntity.ok(ApiResponse.success("Danh sách tổ chức đối tác", partners));
    }

    @GetMapping("/partner-pending-updates")
    public ResponseEntity<ApiResponse<List<PartnerPendingDTO>>> getPendingPartnerUpdates() {
        List<PartnerPendingDTO> pendingList = partnerOrganizationService
                .getPendingPartnerOrganizationUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @GetMapping("/partner-pending-create")
    public ResponseEntity<ApiResponse<List<PartnerInfoDTO>>> getPendingPartnerCreate() {
        List<PartnerInfoDTO> pendingList = partnerOrganizationService.getPendingPartnerOrganizationCreate();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @PostMapping("/approve-partner")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:approve'))")
    public ResponseEntity<ApiResponse<PartnerDTO>> approvePartner(@RequestBody IdReq req) {
        PartnerDTO dto = adminService.approvePartner(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }

    @PostMapping("/approve-partner-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:approve'))")
    public ResponseEntity<ApiResponse<PartnerDTO>> approvePartnerUpdate(@RequestBody IdReq req) {
        PartnerDTO dto = adminService.approvePartnerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-partner")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:approve'))")
    public ResponseEntity<ApiResponse<PartnerDTO>> rejectPartner(@RequestBody RejectReq req) {
        PartnerDTO dto = adminService.rejectPartner(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới", dto));
    }

    @PostMapping("/reject-partner-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:approve'))")
    public ResponseEntity<ApiResponse<PartnerUpdateDTO>> rejectPartnerUpdate(@RequestBody RejectReq req) {
        PartnerUpdateDTO dto = adminService.rejectPartnerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/update-partner")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:update'))")
    public ResponseEntity<ApiResponse<PartnerDTO>> updatePartner(
            @RequestBody PartnerUpdateDTO req) {
        PartnerDTO dto = adminService.updatePartner(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/delete-partner")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:delete'))")
    public ResponseEntity<ApiResponse<Void>> deletePartner(@RequestBody IdReq id) {
        try {
            adminService.deletePartner(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }

    @PostMapping("/create-partner")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:create'))")
    public ResponseEntity<ApiResponse<PartnerDTO>> createPartner(
            @RequestBody CreatePartnerReq req,
            @AuthenticationPrincipal User user) {
        PartnerDTO dto = adminService.createPartner(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    /// Certification
    @PostMapping("/approve-certification")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<CertificationDTO>> approveCertification(@RequestBody IdReq req) {
        CertificationDTO dto = adminService.approveCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }

    @PostMapping("/approve-certification-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<CertificationDTO>> approveCertificationUpdate(@RequestBody IdReq req) {
        CertificationDTO dto = adminService.approveCertificationUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-certification")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<CertificationDTO>> rejectCertification(@RequestBody RejectReq req) {
        CertificationDTO dto = adminService.rejectCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }

    @PostMapping("/reject-certification-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<CertificationDTO>> rejectEditCertification(@RequestBody RejectReq req) {
        CertificationDTO dto = adminService.rejectEditCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/create-certification/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:create'))")
    public ResponseEntity<ApiResponse<CertificationDTO>> createCertification(@RequestBody CertificationCreateReq req,
            @PathVariable UUID lecturerId) {
        CertificationDTO dto = adminService.createCertification(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-certification")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(@RequestBody IdReq req) {
        try {
            adminService.deleteCertification(req);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }

    /// Degree
    @PostMapping("/approve-degree")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<DegreeDTO>> approveDegree(@RequestBody IdReq req) {
        DegreeDTO dto = adminService.approveDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }

    @PostMapping("/approve-degree-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<DegreeDTO>> approveDegreeUpdate(@RequestBody IdReq req) {
        DegreeDTO dto = adminService.approveDegreeUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-degree")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<DegreeDTO>> rejectDegree(@RequestBody RejectReq req) {
        DegreeDTO dto = adminService.rejectDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }

    @PostMapping("/reject-degree-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<DegreeDTO>> rejectDegreeUpdate(@RequestBody RejectReq req) {
        DegreeDTO dto = adminService.rejectDegreeUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/create-degree/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:create'))")
    public ResponseEntity<ApiResponse<DegreeDTO>> createDegree(
            @RequestBody DegreeCreateReq req,
            @PathVariable UUID lecturerId) {

        DegreeDTO dto = adminService.createDegree(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-degree")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteDegree(@RequestBody IdReq req) {
        try {
            adminService.deleteDegree(req);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }

    /// Attended Training Course
    @PostMapping("/approve-attended-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> approveAttendedCourse(@RequestBody IdReq req) {
        AttendedCourseDTO dto = adminService.approveAttendedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }

    @PostMapping("/approve-attended-course-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> approveAttendedCourseUpdate(
            @RequestBody IdReq req) {
        AttendedCourseDTO dto = adminService.approveAttendedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-attended-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> rejectAttendedCourse(@RequestBody RejectReq req) {
        AttendedCourseDTO dto = adminService.rejectAttendedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }

    @PostMapping("/reject-attended-course-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> rejectAttendedCourseUpdate(
            @RequestBody RejectReq req) {
        AttendedCourseDTO dto = adminService.rejectAttendedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/create-attended-course/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:create'))")
    public ResponseEntity<ApiResponse<AttendedCourseDTO>> createAttendedCourse(
            @RequestBody AttendedCourseCreateReq req,
            @PathVariable UUID lecturerId) {

        AttendedCourseDTO dto = adminService.createAttendedCourse(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-attended-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteAttendedCourse(@RequestBody IdReq req) {
        try {
            adminService.deleteAttendedCourse(req);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }

    /// Owned Training Course
    @PostMapping("/approve-owned-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> approveOwnedCourse(@RequestBody IdReq req) {
        OwnedTrainingCourseDTO dto = adminService.approveOwnedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }

    @PostMapping("/approve-owned-course-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> approveOwnedCourseUpdate(@RequestBody IdReq req) {
        OwnedTrainingCourseDTO dto = adminService.approveOwnedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật hành công.", dto));
    }

    @PostMapping("/reject-owned-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> rejectOwnedCourse(@RequestBody RejectReq req) {
        OwnedTrainingCourseDTO dto = adminService.rejectOwnedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }

    @PostMapping("/reject-owned-course-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> rejectOwnedCourseUpdate(@RequestBody RejectReq req) {
        OwnedTrainingCourseDTO dto = adminService.rejectOwnedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/create-owned-course/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:create'))")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> createOwnedCourse(
            @RequestBody OwnedCourseCreateReq req,
            @PathVariable UUID lecturerId) {

        OwnedTrainingCourseDTO dto = adminService.createOwnedCourse(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-owned-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteOwnedCourse(@RequestBody IdReq req) {
        try {
            adminService.deleteOwnedCourse(req);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }

    /// Research Project
    @PostMapping("/approve-research-project")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> approveResearchProject(@RequestBody IdReq req) {
        ResearchProjectDTO dto = adminService.approveResearchProject(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }

    @PostMapping("/approve-research-project-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> approveResearchProjectUpdate(@RequestBody IdReq req) {
        ResearchProjectDTO dto = adminService.approveResearchProjectUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-research-project")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> rejectResearchProject(@RequestBody RejectReq req) {
        ResearchProjectDTO dto = adminService.rejectResearchProject(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }

    @PostMapping("/reject-research-project-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> rejectResearchProjectUpdate(@RequestBody RejectReq req) {
        ResearchProjectDTO dto = adminService.rejectResearchProjectUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/create-research-project/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:create'))")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> createResearchProject(
            @RequestBody ResearchProjectCreateReq req,
            @PathVariable UUID lecturerId) {

        ResearchProjectDTO dto = adminService.createResearchProject(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-research-project")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteResearchProject(@RequestBody IdReq req) {
        try {
            adminService.deleteResearchProject(req);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }

    /// Training Program
    @GetMapping("/get-all-training-programs")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:read'))")
    public ResponseEntity<ApiResponse<List<TrainingProgramDTO>>> getAllTrainingPrograms() {
        List<TrainingProgramDTO> programs = adminService.getAllTrainingPrograms();
        return ResponseEntity.ok(ApiResponse.success("Danh sách chương trình đào tạo", programs));
    }

    @GetMapping("/get-all-training-programs-paginated")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:read'))")
    public ResponseEntity<ApiResponse<PaginatedResponse<TrainingProgramDTO>>> getAllTrainingProgramsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        PaginatedResponse<TrainingProgramDTO> programs = adminService.getAllTrainingProgramsPaginated(page, size);
        return ResponseEntity.ok(ApiResponse.success("Danh sách chương trình đào tạo có phân trang", programs));
    }

    @PostMapping("/create-training-program")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:create'))")
    public ResponseEntity<ApiResponse<TrainingProgramDTO>> createTrainingProgram(@RequestBody TrainingProgramReq req) {
        TrainingProgramDTO dto = adminService.createTrainingProgram(req);
        return ResponseEntity.ok(ApiResponse.success("Tạo chương trình đào tạo thành công", dto));
    }

    @PostMapping("/update-training-program/{programId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:update'))")
    public ResponseEntity<ApiResponse<TrainingProgramDTO>> updateTrainingProgram(@PathVariable UUID programId,
            @RequestBody TrainingProgramReq req) {

        TrainingProgramDTO dto = adminService.updateTrainingProgram(programId, req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật chương trình đào tạo thành công", dto));
    }

    @PostMapping("/archive-training-program/{programId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:archive'))")
    public ResponseEntity<ApiResponse<Void>> archiveTrainingProgram(@PathVariable UUID programId) {
        adminService.archiveTrainingProgram(programId);
        return ResponseEntity.ok(ApiResponse.success("Lưu trữ chương trình đào tạo thành công", null));
    }

    @PostMapping("/unarchive-training-program/{programId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:archive'))")
    public ResponseEntity<ApiResponse<Void>> unarchiveTrainingProgram(@PathVariable UUID programId) {
        adminService.unarchiveTrainingProgram(programId);
        return ResponseEntity.ok(ApiResponse.success("Khôi phục chương trình đào tạo thành công", null));
    }

    @PostMapping("/update-training-program-units/{programId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:update')) or (hasRole('SUB_ADMIN') and hasAuthority('program:create'))")
    public ResponseEntity<ApiResponse<TrainingProgramDTO>> updateProgramUnits(@PathVariable UUID programId,
            @RequestBody List<TrainingUnitDTO> req) {
        TrainingProgramDTO dto = adminService.updateProgramUnits(programId, req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật chương trình đào tạo thành công", dto));
    }

    @PostMapping("/get-all-training-requests-paginated")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:read'))")
    public ResponseEntity<ApiResponse<PaginatedResponse<TrainingProgramRequestDTO>>> getAllTrainingRequestsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginatedResponse<TrainingProgramRequestDTO> requests = adminService.getAllTrainingRequestsPaginated(page,
                size);
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu đào tạo có phân trang", requests));
    }

    @GetMapping("/get-all-program-requests")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:read'))")
    public ResponseEntity<ApiResponse<List<TrainingProgramRequestDTO>>> getAllProgramRequests() {
        List<TrainingProgramRequestDTO> requests = adminService.getAllProgramRequests();
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu đào tạo", requests));
    }

    @PostMapping("/reject-training-program-request")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:update'))")
    public ResponseEntity<ApiResponse<Void>> rejectTrainingProgramRequest(@RequestBody IdReq request) {
        adminService.rejectTrainingProgramRequest(request);
        return ResponseEntity.ok(ApiResponse.success("Từ chối yêu cầu đào tạo thành công", null));
    }

    @PostMapping("/unreject-training-program-request")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('program:update'))")
    public ResponseEntity<ApiResponse<Void>> unrejectTrainingProgramRequest(@RequestBody IdReq request) {
        adminService.unrejectTrainingProgramRequest(request);
        return ResponseEntity.ok(ApiResponse.success("Khôi phục yêu cầu đào tạo thành công", null));
    }

    /// Other

    @GetMapping("/check-citizen-id/{citizenId}")
    public ResponseEntity<ApiResponse<Boolean>> checkCitizenIdExists(@PathVariable("citizenId") String citizenId) {
        Boolean exists = userService.checkCitizenIdExists(citizenId);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra thành công", exists));
    }
    @GetMapping("/lecturer-count")
    public ResponseEntity<ApiResponse<Integer>> getLecturerApprovedCount() {
        Integer count = adminService.getLecturerApprovedCount();
        return ResponseEntity.ok(ApiResponse.success("Lấy số lượng giảng viên đã phê duyệt thành công", count));
    }
    @GetMapping("/institution-count")
    public ResponseEntity<ApiResponse<Integer>> getInstitutionApprovedCount() {
        Integer count = adminService.getInstitutionApprovedCount();
        return ResponseEntity.ok(ApiResponse.success("Lấy số lượng cơ sở giáo dục đã phê duyệt thành công", count));
    }
    @GetMapping("/organization-count")
    public ResponseEntity<ApiResponse<Integer>> getOrganizationApprovedCount() {
        Integer count = adminService.getOrganizationApprovedCount();
        return ResponseEntity.ok(ApiResponse.success("Lấy số lượng tổ chức đối tác đã phê duyệt thành công", count));
    }


}
