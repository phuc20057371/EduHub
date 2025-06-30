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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

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
        if(request.getCitizenId().length()!=11){
            throw new IllegalStateException("id phải có chính xác 11 số");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }

        if (user.getPendingLecturer() != null) {
            throw new IllegalStateException("PendingLecturer profile already exists for this user.");
        }

        PendingLecturer pending = PendingLecturer.builder()
                .citizenId(request.getCitizenId())
                .user(user)
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bio(request.getBio())
                .address(request.getAddress())
                .avatarUrl("https://picsum.photos/200")
                .academicRank(request.getAcademicRank())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())

                .status(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
                .pendingCertifications(new ArrayList<>())
                .pendingDegrees(new ArrayList<>())
                .build();

        // Gán degrees
        if (request.getPendingDegrees() != null) {
            request.getPendingDegrees().forEach(degreeReq -> {
                PendingDegree degree = PendingDegree.builder()
                        .referenceId(degreeReq.getReferenceId())
                        .name(degreeReq.getName())
                        .major(degreeReq.getMajor())
                        .institution(degreeReq.getInstitution())
                        .startYear(degreeReq.getStartYear())
                        .graduationYear(degreeReq.getGraduationYear())
                        .level(degreeReq.getLevel())
                        .url(degreeReq.getUrl())
                        .description(degreeReq.getDescription())

                        .status(PendingStatus.PENDING)
                        .submittedAt(LocalDateTime.now())

                        .pendingLecturer(pending)
                        .build();
                pending.getPendingDegrees().add(degree);
            });
        }

        // Gán certifications
        if (request.getPendingCertifications() != null) {
            request.getPendingCertifications().forEach(certReq -> {
                PendingCertification cert = PendingCertification.builder()
                        .referenceId(certReq.getReferenceId())
                        .name(certReq.getName())
                        .issuedBy(certReq.getIssuedBy())
                        .issueDate(certReq.getIssueDate())
                        .expiryDate(certReq.getExpiryDate())
                        .certificateUrl(certReq.getCertificateUrl())
                        .level(certReq.getLevel())
                        .description(certReq.getDescription())

                        .status(PendingStatus.PENDING)
                        .submittedAt(LocalDateTime.now())

                        .pendingLecturer(pending)
                        .build();
                pending.getPendingCertifications().add(cert);
            });
        }
        PendingLecturer saved = repository.save(pending);

        return LecturerMapper.toPendingLecturerDTO(saved);

    }

    public PendingOwnedTrainingCourseDTO addOwnedCourse(PendingOwnedTrainingCourseRequest request, User currentUser) {
        Integer lecturerId;

        // Determine lecturer ID based on role
        if (currentUser.getRole() == Role.LECTURER) {
            // Giảng viên chỉ được thêm cho chính mình
//            Lecturer lecturer = lecturerRepository.findByUserId(currentUser.getId())
            Lecturer lecturer = lecturerRepository.findById(currentUser.getLecturer().getId())
                    .orElseThrow(() -> new RuntimeException("Lecturer not found for current user"));
            lecturerId = lecturer.getId();
        }  else {
            throw new AccessDeniedException("Only ADMIN or LECTURER can add owned training course");
        }

        // Tìm Lecturer từ ID
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // Ánh xạ từ DTO sang Entity
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

    public PendingAttendedTrainingCourseDTO addAttendedCourse(PendingAttendedTrainingCourseRequest request, User currentUser) {
        Integer lecturerId;

        if (currentUser.getRole() == Role.LECTURER) {
            Lecturer lecturer = lecturerRepository.findById(currentUser.getLecturer().getId())
                    .orElseThrow(() -> new RuntimeException("Lecturer not found for current user"));
            lecturerId = lecturer.getId();
        } else {
            throw new AccessDeniedException("Only ADMIN or LECTURER can add attended training course");
        }

        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

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

    public PendingResearchProjectDTO addResearchProject(PendingResearchProjectRequest request, User currentUser) {
        Integer lecturerId;

        if (currentUser.getRole() == Role.LECTURER) {
            Lecturer lecturer = lecturerRepository.findById(currentUser.getLecturer().getId())
                    .orElseThrow(() -> new RuntimeException("Lecturer not found for current user"));
            lecturerId = lecturer.getId();
        }  else {
            throw new AccessDeniedException("Only ADMIN or LECTURER can add research project");
        }

        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

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

    public PendingLecturerDTO updatePendingLecturer(PendingLecturerUpdateRequest request) {
        if (request.getCitizenId() == null || request.getCitizenId().length() != 11) {
            throw new IllegalStateException("Số CCCD phải có chính xác 11 số");
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }

        // Tìm hồ sơ pending của người dùng hiện tại
        PendingLecturer pending = repository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));

        // Đảm bảo chỉ chính chủ được chỉnh sửa hồ sơ của họ
        if (!pending.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền cập nhật hồ sơ này");
        }

        // Cập nhật các trường thông tin cơ bản
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
        // Reset trạng thái để admin duyệt lại
        pending.setStatus(PendingStatus.PENDING);
        pending.setUpdatedAt(LocalDateTime.now());

        PendingLecturer updated = repository.save(pending);

        return LecturerMapper.toPendingLecturerDTO(updated);
    }


    @Transactional
    public PendingCertificationDTO updatePendingCertification(PendingCertificationRequest request, User user) {
        PendingCertification cert = pendingCertificationRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("PendingCertification not found"));

        if (!cert.getPendingLecturer().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền chỉnh sửa chứng chỉ này");
        }

        // Cập nhật thông tin
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
//        Degree original = degreeRepository.findById(dto.getOriginalId())
//                .orElseThrow(() -> new RuntimeException("Degree not found"));

        PendingDegree pending = pendingDegreeRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("PendingDegree not found"));

        if (!pending.getPendingLecturer().getUser().getId().equals(user.getId())) {
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
        PendingLecturer pendingLecturer = repository.findById(user.getPendingLecturer().getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found for user"));

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
//                .originalId(dto.getOriginalId())
                .submittedAt(LocalDateTime.now())
                .pendingLecturer(pendingLecturer)
                .build();

        PendingDegree saved = pendingDegreeRepository.save(degree);
        return LecturerMapper.toPendingDegreeDTO(saved);
    }

    @Transactional
    public PendingCertificationDTO createPendingCertification(PendingCertificationRequest request, User user) {
        PendingLecturer pendingLecturer = repository.findById(user.getPendingLecturer().getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found for user"));

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
//                .originalId(dto.getOriginalId())
                .submittedAt(LocalDateTime.now())
                .pendingLecturer(pendingLecturer)
                .build();

        PendingCertification saved = pendingCertificationRepository.save(cert);
        return LecturerMapper.toPendingCertificationDTO(saved);
    }

    @Transactional
    public PendingLecturerDTO resubmitPendingLecturer(PendingLecturerDTO request, User user) {
        if (request.getCitizenId().length() != 11) {
            throw new IllegalStateException("Citizen ID phải có 11 số");
        }

        PendingLecturer pending = repository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer không tồn tại"));

        if (!pending.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Bạn không có quyền sửa hồ sơ này");
        }

        // ✅ Reset lại trạng thái để xét duyệt lại
        pending.setStatus(PendingStatus.PENDING);
        pending.setResponse(null);
        pending.setUpdatedAt(LocalDateTime.now());

        // ✅ Cập nhật thông tin cơ bản
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

        // ✅ Xử lý degree mới: tạo lại danh sách
        pending.getPendingDegrees().clear();
        if (request.getPendingDegrees() != null) {
            for (PendingDegreeDTO d : request.getPendingDegrees()) {
                pending.getPendingDegrees().add(PendingDegree.builder()
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
                        .build());
            }
        }

        // ✅ Xử lý certification mới
        pending.getPendingCertifications().clear();
        if (request.getPendingCertifications() != null) {
            for (PendingCertificationDTO c : request.getPendingCertifications()) {
                pending.getPendingCertifications().add(PendingCertification.builder()
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
                        .build());
            }
        }

        PendingLecturer updated = repository.save(pending);
        return LecturerMapper.toPendingLecturerDTO(updated);
    }
}
