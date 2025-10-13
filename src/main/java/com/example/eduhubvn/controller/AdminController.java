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

import com.example.eduhubvn.dtos.AllPendingEntityDTO;
import com.example.eduhubvn.dtos.AllPendingUpdateDTO;
import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.PaginatedResponse;
import com.example.eduhubvn.dtos.RejectReq;
import com.example.eduhubvn.dtos.RequestFromLecturer;
import com.example.eduhubvn.dtos.admin.request.RegisterInstitutionFromAdminRequest;
import com.example.eduhubvn.dtos.admin.request.RegisterLecturerFromAdminRequest;
import com.example.eduhubvn.dtos.admin.request.RegisterPartnerFromAdminRequest;
import com.example.eduhubvn.dtos.course.OwnedCourseInfoDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.edu.InstitutionInfoDTO;
import com.example.eduhubvn.dtos.lecturer.AttendedTrainingCourseDTO;
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
import com.example.eduhubvn.dtos.lecturer.request.AttendedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.ResearchProjectReq;
import com.example.eduhubvn.dtos.partner.PartnerInfoDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.AdminService;
import com.example.eduhubvn.services.EducationInstitutionService;
import com.example.eduhubvn.services.LecturerService;
import com.example.eduhubvn.services.PartnerOrganizationService;
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
    private final EducationInstitutionService educationInstitutionService;
    private final PartnerOrganizationService partnerOrganizationService;

    /// General

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
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturer(@RequestBody IdRequest req) {
        LecturerDTO dto = adminService.approveLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Duyệt thành công", dto));
    }

    @PostMapping("/approve-lecturer-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<LecturerDTO>> approveLecturerUpdate(@RequestBody IdRequest req) {
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
    public ResponseEntity<ApiResponse<Void>> deleteLecturer(@RequestBody IdRequest req) {
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
    public ResponseEntity<ApiResponse<LecturerDTO>> createLecturer(@RequestBody RegisterLecturerFromAdminRequest req,
            @AuthenticationPrincipal User user) {
        LecturerDTO dto = adminService.createLecturer(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    /// Education Institution

    @GetMapping("/get-all-institutions")
    public ResponseEntity<ApiResponse<List<EducationInstitutionDTO>>> getAllInstitutions() {
        List<EducationInstitutionDTO> institutions = adminService.getAllInstitutions();
        return ResponseEntity.ok(ApiResponse.success("Danh sách cơ sở giáo dục", institutions));
    }

    @GetMapping("/institution-pending-updates")
    public ResponseEntity<ApiResponse<List<EducationInstitutionPendingDTO>>> getPendingEduInstitutionUpdates() {
        List<EducationInstitutionPendingDTO> pendingList = educationInstitutionService
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
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> approveEduIns(@RequestBody IdRequest req) {
        EducationInstitutionDTO dto = adminService.approveEduIns(req);
        return ResponseEntity.ok(ApiResponse.success("Duyệt thành công", dto));
    }

    @PostMapping("/approve-institution-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:approve'))")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> approveEduInsUpdate(@RequestBody IdRequest req) {
        EducationInstitutionDTO dto = adminService.approveEduInsUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-institution")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:approve'))")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> rejectInstitution(@RequestBody RejectReq req) {
        EducationInstitutionDTO dto = adminService.rejectInstitution(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới", dto));
    }

    @PostMapping("/reject-institution-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:approve'))")
    public ResponseEntity<ApiResponse<EducationInstitutionUpdateDTO>> rejectEduInsUpdate(@RequestBody RejectReq req) {
        EducationInstitutionUpdateDTO dto = adminService.rejectEduInsUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/update-institution")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:update'))")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> updateInstitution(
            @RequestBody EducationInstitutionUpdateDTO req) {
        EducationInstitutionDTO dto = adminService.updateInstitution(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('school:delete'))")
    @PostMapping("/delete-institution")
    public ResponseEntity<ApiResponse<Void>> deleteInstitution(@RequestBody IdRequest id) {
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
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> createInstitution(
            @RequestBody RegisterInstitutionFromAdminRequest req,
            @AuthenticationPrincipal User user) {
        EducationInstitutionDTO dto = adminService.createInstitution(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    /// Partner Organization

    @GetMapping("/get-all-partners")
    public ResponseEntity<ApiResponse<List<PartnerOrganizationDTO>>> getAllPartners() {
        List<PartnerOrganizationDTO> partners = adminService.getAllPartners();
        return ResponseEntity.ok(ApiResponse.success("Danh sách tổ chức đối tác", partners));
    }

    @GetMapping("/partner-pending-updates")
    public ResponseEntity<ApiResponse<List<PartnerOrganizationPendingDTO>>> getPendingPartnerUpdates() {
        List<PartnerOrganizationPendingDTO> pendingList = partnerOrganizationService
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
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> approvePartner(@RequestBody IdRequest req) {
        PartnerOrganizationDTO dto = adminService.approvePartner(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }

    @PostMapping("/approve-partner-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:approve'))")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> approvePartnerUpdate(@RequestBody IdRequest req) {
        PartnerOrganizationDTO dto = adminService.approvePartnerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-partner")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:approve'))")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> rejectPartner(@RequestBody RejectReq req) {
        PartnerOrganizationDTO dto = adminService.rejectPartner(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới", dto));
    }

    @PostMapping("/reject-partner-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:approve'))")
    public ResponseEntity<ApiResponse<PartnerOrganizationUpdateDTO>> rejectPartnerUpdate(@RequestBody RejectReq req) {
        PartnerOrganizationUpdateDTO dto = adminService.rejectPartnerUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/update-partner")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:update'))")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> updatePartner(
            @RequestBody PartnerOrganizationUpdateDTO req) {
        PartnerOrganizationDTO dto = adminService.updatePartner(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/delete-partner")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('organization:delete'))")
    public ResponseEntity<ApiResponse<Void>> deletePartner(@RequestBody IdRequest id) {
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
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> createPartner(
            @RequestBody RegisterPartnerFromAdminRequest req,
            @AuthenticationPrincipal User user) {
        PartnerOrganizationDTO dto = adminService.createPartner(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    /// Certification
    @PostMapping("/approve-certification")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<CertificationDTO>> approveCertification(@RequestBody IdRequest req) {
        CertificationDTO dto = adminService.approveCertification(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }

    @PostMapping("/approve-certification-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<CertificationDTO>> approveCertificationUpdate(@RequestBody IdRequest req) {
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
    public ResponseEntity<ApiResponse<CertificationDTO>> createCertification(@RequestBody CertificationReq req,
            @PathVariable UUID lecturerId) {
        CertificationDTO dto = adminService.createCertification(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-certification")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(@RequestBody IdRequest req) {
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
    public ResponseEntity<ApiResponse<DegreeDTO>> approveDegree(@RequestBody IdRequest req) {
        DegreeDTO dto = adminService.approveDegree(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công", dto));
    }

    @PostMapping("/approve-degree-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<DegreeDTO>> approveDegreeUpdate(@RequestBody IdRequest req) {
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
            @RequestBody DegreeReq req,
            @PathVariable UUID lecturerId) {

        DegreeDTO dto = adminService.createDegree(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-degree")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteDegree(@RequestBody IdRequest req) {
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
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> approveAttendedCourse(@RequestBody IdRequest req) {
        AttendedTrainingCourseDTO dto = adminService.approveAttendedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }

    @PostMapping("/approve-attended-course-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> approveAttendedCourseUpdate(
            @RequestBody IdRequest req) {
        AttendedTrainingCourseDTO dto = adminService.approveAttendedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
    }

    @PostMapping("/reject-attended-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> rejectAttendedCourse(@RequestBody RejectReq req) {
        AttendedTrainingCourseDTO dto = adminService.rejectAttendedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối tạo mới.", dto));
    }

    @PostMapping("/reject-attended-course-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> rejectAttendedCourseUpdate(
            @RequestBody RejectReq req) {
        AttendedTrainingCourseDTO dto = adminService.rejectAttendedCourseUpdate(req);
        return ResponseEntity.ok(ApiResponse.success("Từ chối cập nhật.", dto));
    }

    @PostMapping("/create-attended-course/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:create'))")
    public ResponseEntity<ApiResponse<AttendedTrainingCourseDTO>> createAttendedCourse(
            @RequestBody AttendedTrainingCourseReq req,
            @PathVariable UUID lecturerId) {

        AttendedTrainingCourseDTO dto = adminService.createAttendedCourse(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-attended-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteAttendedCourse(@RequestBody IdRequest req) {
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
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> approveOwnedCourse(@RequestBody IdRequest req) {
        OwnedTrainingCourseDTO dto = adminService.approveOwnedCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }

    @PostMapping("/approve-owned-course-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> approveOwnedCourseUpdate(@RequestBody IdRequest req) {
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

    @GetMapping("/get-new-owned-courses")
    public ResponseEntity<ApiResponse<List<OwnedCourseInfoDTO>>> getOwnedCourses() {
        List<OwnedCourseInfoDTO> ownedCourses = adminService.getOwnedCourses();
        return ResponseEntity.ok(ApiResponse.success("Danh sách khóa học sở hữu", ownedCourses));
    }

    @PostMapping("/create-owned-course/{lecturerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:create'))")
    public ResponseEntity<ApiResponse<OwnedTrainingCourseDTO>> createOwnedCourse(
            @RequestBody OwnedTrainingCourseReq req,
            @PathVariable UUID lecturerId) {

        OwnedTrainingCourseDTO dto = adminService.createOwnedCourse(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-owned-course")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteOwnedCourse(@RequestBody IdRequest req) {
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
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> approveResearchProject(@RequestBody IdRequest req) {
        ResearchProjectDTO dto = adminService.approveResearchProject(req);
        return ResponseEntity.ok(ApiResponse.success("Thành công.", dto));
    }

    @PostMapping("/approve-research-project-update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:approve'))")
    public ResponseEntity<ApiResponse<ResearchProjectDTO>> approveResearchProjectUpdate(@RequestBody IdRequest req) {
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
            @RequestBody ResearchProjectReq req,
            @PathVariable UUID lecturerId) {

        ResearchProjectDTO dto = adminService.createResearchProject(req, lecturerId);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu tạo mới", dto));
    }

    @PostMapping("/delete-research-project")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SUB_ADMIN') and hasAuthority('lecturer:delete'))")
    public ResponseEntity<ApiResponse<Void>> deleteResearchProject(@RequestBody IdRequest req) {
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

    /// Course





    /// Other

    @GetMapping("/check-citizen-id/{citizenId}")
    public ResponseEntity<ApiResponse<Boolean>> checkCitizenIdExists(@PathVariable("citizenId") String citizenId) {
        Boolean exists = userService.checkCitizenIdExists(citizenId);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra thành công", exists));
    }

}
