package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.pendingCourse.PendingAttendedTrainingCourseRequest;
import com.example.eduhubvn.dtos.lecturer.pendingCourse.PendingOwnedTrainingCourseRequest;
import com.example.eduhubvn.dtos.lecturer.pendingCourse.PendingResearchProjectRequest;
import com.example.eduhubvn.dtos.lecturer.request.PendingCertificationRequest;
import com.example.eduhubvn.dtos.lecturer.request.PendingDegreeRequest;
import com.example.eduhubvn.dtos.lecturer.request.PendingLecturerUpdateRequest;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PendingLecturerService {
    private final PendingLecturerRepository repository;
    private final LecturerRepository lecturerRepository;

    private final CertificationRepository certificationRepository;
    private final DegreeRepository degreeRepository;

    private final PendingOwnedTrainingCourseRepository pendingOwnedTrainingCourseRepository;
    private final PendingAttendedTrainingCourseRepository pendingAttendedTrainingCourseRepository;
    private final PendingResearchProjectRepository pendingResearchProjectRepository;

    private final PendingCertificationRepository pendingCertificationRepository;
    private final PendingDegreeRepository pendingDegreeRepository;


    @Transactional
    public PendingLecturerDTO createPendingLecturer(PendingLecturerDTO request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy người dùng đang đăng nhập.");
        }
        if (request.getCitizenId() == null || request.getCitizenId().length() != 11) {
            throw new IllegalArgumentException("Số CCCD phải có chính xác 11 chữ số.");
        }
        if (user.getPendingLecturer() != null) {
            throw new IllegalStateException("Bạn đã có hồ sơ PendingLecturer rồi.");
        }
        PendingLecturer pending = PendingLecturer.builder()
                .citizenId(request.getCitizenId())
                .user(user)
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bio(request.getBio())
                .address(request.getAddress())
                .avatarUrl(request.getAvatarUrl() != null ? request.getAvatarUrl() : "https://picsum.photos/200")
                .academicRank(request.getAcademicRank())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .status(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .pendingCertifications(new ArrayList<>())
                .pendingDegrees(new ArrayList<>())
                .build();

        mapPendingDegrees(request, pending);
        mapPendingCertifications(request, pending);

        PendingLecturer saved = repository.save(pending);
        return LecturerMapper.toPendingLecturerDTO(saved);
    }


    @Transactional
    public PendingOwnedTrainingCourseDTO addOwnedCourse(PendingOwnedTrainingCourseRequest request, User currentUser) {
        if (request == null) {
            throw new IllegalArgumentException("Request không được để trống");
        }
        if (currentUser.getRole() != Role.LECTURER) {
            throw new AccessDeniedException("Chỉ giảng viên mới có thể thêm khóa học sở hữu");
        }
        Lecturer lecturerRef = currentUser.getLecturer();
        if (lecturerRef == null) {
            throw new EntityNotFoundException("Không tìm thấy hồ sơ giảng viên");
        }
        Lecturer lecturer = lecturerRepository.findById(lecturerRef.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên trong hệ thống"));
//        // Validate dữ liệu đầu vào (tùy mức độ bạn muốn kiểm tra)
//        if (request.getTitle() == null || request.getTitle().isBlank()) {
//            throw new IllegalArgumentException("Tiêu đề khóa học không được để trống");
//        }
        PendingOwnedTrainingCourse course = PendingOwnedTrainingCourse.builder()
                .title(request.getTitle())
                .topic(request.getTopic())
                .courseType(request.getCourseType())
                .scale(request.getScale())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .numberOfHour(request.getNumberOfHour())
                .location(request.getLocation())
                .status(request.getStatus())
                .description(request.getDescription())
                .courseUrl(request.getCourseUrl())
                .lecturer(lecturer)
                .pendingStatus(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        pendingOwnedTrainingCourseRepository.save(course);
        return LecturerMapper.pendingOwnedTrainingCourseDTO(course);
    }


    @Transactional
    public PendingAttendedTrainingCourseDTO addAttendedCourse(PendingAttendedTrainingCourseRequest request, User currentUser) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu không được để trống");
        }
        if (currentUser.getRole() != Role.LECTURER) {
            throw new AccessDeniedException("Chỉ giảng viên mới có thể thêm khóa học đã tham gia");
        }
        Lecturer lecturerRef = currentUser.getLecturer();
        if (lecturerRef == null) {
            throw new EntityNotFoundException("Không tìm thấy hồ sơ giảng viên");
        }
        Lecturer lecturer = lecturerRepository.findById(lecturerRef.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên trong hệ thống"));
//        // Validate các field quan trọng (tùy yêu cầu nghiệp vụ)
//        if (request.getTitle() == null || request.getTitle().isBlank()) {
//            throw new IllegalArgumentException("Tiêu đề không được để trống");
//        }

        PendingAttendedTrainingCourse course = PendingAttendedTrainingCourse.builder()
                .title(request.getTitle())
                .topic(request.getTopic())
                .organizer(request.getOrganizer())
                .courseType(request.getCourseType())
                .scale(request.getScale())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .numberOfHour(request.getNumberOfHour())
                .location(request.getLocation())
                .description(request.getDescription())
                .courseUrl(request.getCourseUrl())
                .originalId(null)
                .pendingStatus(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .lecturer(lecturer)
                .build();
        pendingAttendedTrainingCourseRepository.save(course);
        return LecturerMapper.mapToAttendedDTO(course);
    }


    @Transactional
    public PendingResearchProjectDTO addResearchProject(PendingResearchProjectRequest request, User currentUser) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu không được để trống");
        }
        if (currentUser.getRole() != Role.LECTURER) {
            throw new AccessDeniedException("Chỉ giảng viên mới có thể thêm đề tài nghiên cứu");
        }
        Lecturer lecturerRef = currentUser.getLecturer();
        if (lecturerRef == null) {
            throw new EntityNotFoundException("Không tìm thấy hồ sơ giảng viên");
        }
        Lecturer lecturer = lecturerRepository.findById(lecturerRef.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên trong hệ thống"));
        // validate dữ liệu)
        // if (request.getTitle() == null || request.getTitle().isBlank()) {
        //     throw new IllegalArgumentException("Tên đề tài không được để trống");
        // }
        PendingResearchProject project = PendingResearchProject.builder()
                .title(request.getTitle())
                .researchArea(request.getResearchArea())
                .scale(request.getScale())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .foundingAmount(request.getFoundingAmount())
                .foundingSource(request.getFoundingSource())
                .projectType(request.getProjectType())
                .roleInProject(request.getRoleInProject())
                .publishedUrl(request.getPublishedUrl())
                .status(request.getStatus())
                .description(request.getDescription())
                .originalId(null)
                .pendingStatus(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .lecturer(lecturer)
                .build();
        pendingResearchProjectRepository.save(project);
        return LecturerMapper.mapToResearchProjectDTO(project);
    }



    @Transactional
    public PendingLecturerDTO updatePendingLecturer(PendingLecturerUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không được để trống");
        }
        if (request.getCitizenId() == null || request.getCitizenId().length() != 11) {
            throw new IllegalArgumentException("Số CCCD phải có chính xác 11 số");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy người dùng đang đăng nhập");
        }
        PendingLecturer pending = repository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ PendingLecturer"));
        if (!pending.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền cập nhật hồ sơ này");
        }
        pending.setCitizenId(request.getCitizenId());
        pending.setFullName(request.getFullName());
        pending.setDateOfBirth(request.getDateOfBirth());
        pending.setGender(request.getGender());
        pending.setBio(request.getBio());
        pending.setAddress(request.getAddress());
        pending.setAvatarUrl(request.getAvatarUrl());
        pending.setAcademicRank(request.getAcademicRank());
        pending.setSpecialization(request.getSpecialization());
        pending.setExperienceYears(request.getExperienceYears());

        pending.setResponse("");
        pending.setStatus(PendingStatus.PENDING);
        pending.setUpdatedAt(LocalDateTime.now());

        PendingLecturer updated = repository.save(pending);
        return LecturerMapper.toPendingLecturerDTO(updated);
    }



    @Transactional
    public PendingCertificationDTO updatePendingCertification(PendingCertificationRequest request, User user) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu cập nhật chứng chỉ không được để trống");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("ID của chứng chỉ cần cập nhật không được để trống");
        }
        PendingCertification cert = pendingCertificationRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chứng chỉ cần cập nhật"));

        if (cert.getPendingLecturer() == null || cert.getPendingLecturer().getUser() == null
                || !cert.getPendingLecturer().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền chỉnh sửa chứng chỉ này");
        }

        cert.setReferenceId(request.getReferenceId());
        cert.setName(request.getName());
        cert.setIssuedBy(request.getIssuedBy());
        cert.setIssueDate(request.getIssueDate());
        cert.setExpiryDate(request.getExpiryDate());
        cert.setCertificateUrl(request.getCertificateUrl());
        cert.setLevel(request.getLevel());
        cert.setDescription(request.getDescription());

        cert.setReason("");
        cert.setStatus(PendingStatus.PENDING);
        cert.setUpdatedAt(LocalDateTime.now());

        return LecturerMapper.toPendingCertificationDTO(cert);
    }

    @Transactional
    public PendingDegreeDTO updatePendingDegree(PendingDegreeRequest request, User user) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu cập nhật bằng cấp không được để trống");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("ID của bằng cấp cần cập nhật không được để trống");
        }
        PendingDegree pending = pendingDegreeRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bằng cấp cần cập nhật"));
        User owner = Optional.ofNullable(pending.getPendingLecturer())
                .map(PendingLecturer::getUser)
                .orElseThrow(() -> new AccessDeniedException("Bằng cấp không liên kết với người dùng hợp lệ"));

        if (!owner.getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền chỉnh sửa bằng cấp này");
        }
        pending.setReferenceId(request.getReferenceId());
        pending.setName(request.getName());
        pending.setMajor(request.getMajor());
        pending.setInstitution(request.getInstitution());
        pending.setStartYear(request.getStartYear());
        pending.setGraduationYear(request.getGraduationYear());
        pending.setLevel(request.getLevel());
        pending.setUrl(request.getUrl());
        pending.setDescription(request.getDescription());

        pending.setStatus(PendingStatus.PENDING);
        pending.setReason("");
        pending.setUpdatedAt(LocalDateTime.now());

        return LecturerMapper.toPendingDegreeDTO(pending);
    }

    @Transactional
    public PendingDegreeDTO createPendingDegree(PendingDegreeRequest request, User user) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu tạo bằng cấp không được để trống");
        }
        Integer pendingLecturerId = Optional.ofNullable(user.getPendingLecturer())
                .map(PendingLecturer::getId)
                .orElseThrow(() -> new IllegalStateException("Người dùng chưa có hồ sơ giảng viên đang xét duyệt"));
        PendingLecturer pendingLecturer = repository.findById(pendingLecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ giảng viên đang xét duyệt"));
        PendingDegree degree = PendingDegree.builder()
                .referenceId(request.getReferenceId())
                .name(request.getName())
                .major(request.getMajor())
                .institution(request.getInstitution())
                .startYear(request.getStartYear())
                .graduationYear(request.getGraduationYear())
                .level(request.getLevel())
                .url(request.getUrl())
                .description(request.getDescription())
                .status(PendingStatus.PENDING)
                .submittedAt(LocalDateTime.now())
                .pendingLecturer(pendingLecturer)
                .build();

        PendingDegree saved = pendingDegreeRepository.save(degree);
        return LecturerMapper.toPendingDegreeDTO(saved);
    }


    @Transactional
    public PendingCertificationDTO createPendingCertification(PendingCertificationRequest request, User user) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu tạo chứng chỉ không được để trống");
        }
        Integer pendingLecturerId = Optional.ofNullable(user.getPendingLecturer())
                .map(PendingLecturer::getId)
                .orElseThrow(() -> new IllegalStateException("Người dùng chưa có hồ sơ giảng viên đang xét duyệt"));

        PendingLecturer pendingLecturer = repository.findById(pendingLecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ giảng viên đang xét duyệt"));
        PendingCertification cert = PendingCertification.builder()
                .referenceId(request.getReferenceId())
                .name(request.getName())
                .issuedBy(request.getIssuedBy())
                .issueDate(request.getIssueDate())
                .expiryDate(request.getExpiryDate())
                .certificateUrl(request.getCertificateUrl())
                .level(request.getLevel())
                .description(request.getDescription())
                .status(PendingStatus.PENDING)
                .submittedAt(LocalDateTime.now())
                .pendingLecturer(pendingLecturer)
                .build();

        PendingCertification saved = pendingCertificationRepository.save(cert);
        return LecturerMapper.toPendingCertificationDTO(saved);
    }


    @Transactional
    public PendingLecturerDTO resubmitPendingLecturer(PendingLecturerDTO request, User user) {
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu gửi lên không được để trống");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("ID của hồ sơ pending không được để trống");
        }
        if (request.getCitizenId() == null || request.getCitizenId().length() != 11) {
            throw new IllegalStateException("Citizen ID phải có chính xác 11 số");
        }

        PendingLecturer pending = repository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ PendingLecturer"));

        if (!pending.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền sửa hồ sơ này");
        }

        pending.setCitizenId(request.getCitizenId());
        pending.setFullName(request.getFullName());
        pending.setDateOfBirth(request.getDateOfBirth());
        pending.setGender(request.getGender());
        pending.setBio(request.getBio());
        pending.setAddress(request.getAddress());
        pending.setAvatarUrl(request.getAvatarUrl());
        pending.setAcademicRank(request.getAcademicRank());
        pending.setSpecialization(request.getSpecialization());
        pending.setExperienceYears(request.getExperienceYears());

        pending.setStatus(PendingStatus.PENDING);
        pending.setResponse(null);
        pending.setUpdatedAt(LocalDateTime.now());

        pending.getPendingDegrees().clear();
        pending.getPendingDegrees().addAll(mapPendingDegrees(request, pending));

        pending.getPendingCertifications().clear();
        pending.getPendingCertifications().addAll(mapPendingCertifications(request, pending));

        PendingLecturer updated = repository.save(pending);
        return LecturerMapper.toPendingLecturerDTO(updated);
    }








    private List<PendingDegree> mapPendingDegrees(PendingLecturerDTO request, PendingLecturer pending) {
        if (request.getPendingDegrees() == null) return Collections.emptyList();

        return request.getPendingDegrees().stream()
                .map(d -> PendingDegree.builder()
                        .referenceId(d.getReferenceId())
                        .name(d.getName())
                        .major(d.getMajor())
                        .institution(d.getInstitution())
                        .startYear(d.getStartYear())
                        .graduationYear(d.getGraduationYear())
                        .level(d.getLevel())
                        .url(d.getUrl())
                        .description(d.getDescription())
                        .status(PendingStatus.PENDING)
                        .submittedAt(LocalDateTime.now())
                        .pendingLecturer(pending)
                        .build())
                .collect(Collectors.toList());
    }

    private List<PendingCertification> mapPendingCertifications(PendingLecturerDTO request, PendingLecturer pending) {
        if (request.getPendingCertifications() == null) return Collections.emptyList();

        return request.getPendingCertifications().stream()
                .map(c -> PendingCertification.builder()
                        .referenceId(c.getReferenceId())
                        .name(c.getName())
                        .issuedBy(c.getIssuedBy())
                        .issueDate(c.getIssueDate())
                        .expiryDate(c.getExpiryDate())
                        .certificateUrl(c.getCertificateUrl())
                        .level(c.getLevel())
                        .description(c.getDescription())
                        .status(PendingStatus.PENDING)
                        .submittedAt(LocalDateTime.now())
                        .pendingLecturer(pending)
                        .build())
                .collect(Collectors.toList());
    }

}
