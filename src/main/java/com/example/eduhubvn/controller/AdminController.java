package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.*;
import com.example.eduhubvn.dtos.course.CourseDTO;
import com.example.eduhubvn.dtos.course.CourseInfoDTO;
import com.example.eduhubvn.dtos.course.CourseReq;
import com.example.eduhubvn.dtos.course.OwnedCourseInfoDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.entities.Course;
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
    private final UserService userService;




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

    @GetMapping("/partner-pending-updates")
    public ResponseEntity<ApiResponse<List<PartnerOrganizationPendingDTO>>> getPendingPartnerUpdates() {
        List<PartnerOrganizationPendingDTO> pendingList = partnerOrganizationService.getPendingPartnerOrganizationUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @GetMapping("/partner-pending-create")
    public ResponseEntity<ApiResponse<List<PartnerOrganizationDTO>>> getPendingPartnerCreate() {
        List<PartnerOrganizationDTO> pendingList = partnerOrganizationService.getPendingPartnerOrganizationCreate();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @GetMapping("/institution-pending-updates")
    public ResponseEntity<ApiResponse<List<EducationInstitutionPendingDTO>>> getPendingEduInstitutionUpdates() {
        List<EducationInstitutionPendingDTO> pendingList = educationInstitutionService.getPendingEducationInstitutionUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @GetMapping("/institution-pending-create")
    public ResponseEntity<ApiResponse<List<EducationInstitutionDTO>>> getPendingEduInstitutionCreate() {
        List<EducationInstitutionDTO> pendingList = educationInstitutionService.getPendingEducationInstitutionCreate();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }

    @GetMapping("/degree-pending-updates")
    public ResponseEntity<ApiResponse<List<DegreeUpdateDTO>>> getPendingDegreeUpdates() {
        List<DegreeUpdateDTO> pendingList = adminService.getPendingDegreeUpdates();
        return ResponseEntity.ok(ApiResponse.success("Danh sách đang chờ duyệt", pendingList));
    }


    @GetMapping("/degree-pending-create")
    public ResponseEntity<ApiResponse<List<DegreePendingCreateDTO>>> getPendingDegreeCreate() {
        List<DegreePendingCreateDTO> pendingList = adminService.getPendingDegreeCreate();
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


    @GetMapping("/get-all-lecturers")
    public ResponseEntity<ApiResponse<List<LecturerInfoDTO>>> getAllLecturers() {
        List<LecturerInfoDTO> lecturers = adminService.getAllLecturers();
        return ResponseEntity.ok(ApiResponse.success("Danh sách giảng viên", lecturers));
    }

    @GetMapping("/get-all-institutions")
    public ResponseEntity<ApiResponse<List<EducationInstitutionDTO>>> getAllInstitutions() {
        List<EducationInstitutionDTO> institutions = adminService.getAllInstitutions();
        return ResponseEntity.ok(ApiResponse.success("Danh sách cơ sở giáo dục", institutions));
    }

    @GetMapping("/get-all-partners")
    public ResponseEntity<ApiResponse<List<PartnerOrganizationDTO>>> getAllPartners() {
        List<PartnerOrganizationDTO> partners = adminService.getAllPartners();
        return ResponseEntity.ok(ApiResponse.success("Danh sách tổ chức đối tác", partners));
    }

    @GetMapping("/get-lecturer-requests")
    public ResponseEntity<ApiResponse<List<RequestFromLecturer<?>>>> getLecturerRequests() {
        List<RequestFromLecturer<?>> requests = adminService.getLecturerRequests();
        return ResponseEntity.ok(ApiResponse.success("Danh sách yêu cầu từ giảng viên", requests));
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
    @PostMapping("/update-lecturer")
    public ResponseEntity<ApiResponse<LecturerDTO>> updateLecturer(@RequestBody LecturerUpdateDTO req) {
        LecturerDTO dto = adminService.updateLecturer(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
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

    @PostMapping("/update-institution")
    public ResponseEntity<ApiResponse<EducationInstitutionDTO>> updateInstitution(@RequestBody EducationInstitutionUpdateDTO req) {
        EducationInstitutionDTO dto = adminService.updateInstitution(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
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
    @PostMapping("/update-partner")
    public ResponseEntity<ApiResponse<PartnerOrganizationDTO>> updatePartner(@RequestBody PartnerOrganizationUpdateDTO req) {
        PartnerOrganizationDTO dto = adminService.updatePartner(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công.", dto));
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
    @PostMapping("/reject-certification-update")
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
    @PostMapping("/reject-degree-update")
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
    @GetMapping("/get-new-owned-courses")
    public ResponseEntity<ApiResponse<List<OwnedCourseInfoDTO>>> getOwnedCourses() {
        List<OwnedCourseInfoDTO> ownedCourses = adminService.getOwnedCourses();
        return ResponseEntity.ok(ApiResponse.success("Danh sách khóa học sở hữu", ownedCourses));
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

/// Course
    @GetMapping("get-all-courses")
    public ResponseEntity<ApiResponse<List<CourseInfoDTO>>> getAllCourses() {
        List<CourseInfoDTO> courses = adminService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success("Danh sách khóa học", courses));
    }
    @GetMapping("/get-course/{id}")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseById(@PathVariable("id") String id) {
        CourseDTO course = adminService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success("Thông tin khóa học", course));
    }
    @PostMapping("/update-course-member")
    public ResponseEntity<ApiResponse<CourseInfoDTO>> updateCourseMember(@RequestBody CourseInfoDTO req) {
        CourseInfoDTO courseInfo = adminService.updateCourseMember(req);
        return ResponseEntity.ok(ApiResponse.success("Thêm thành viên vào khóa học thành công", courseInfo));
    }
    @PostMapping("create-course")
    public ResponseEntity<ApiResponse<CourseInfoDTO>> createCourse(@RequestBody CourseReq req) {
        CourseInfoDTO course = adminService.createCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Tạo khóa học thành công", course));
    }

    @GetMapping("/check-citizen-id/{citizenId}")
    public ResponseEntity<ApiResponse<Boolean>> checkCitizenIdExists(@PathVariable("citizenId") String citizenId) {
        Boolean exists = userService.checkCitizenIdExists(citizenId);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra thành công", exists));
    }
    @PostMapping("/update-course")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(@RequestBody CourseDTO req) {
        CourseDTO course = adminService.updateCourse(req);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật khóa học thành công", course));
    }

}
