package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.admin.ApplicationPendingReject;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.RejectPendingLecturerRequest;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final PendingLecturerRepository pendingLecturerRepository;

    private final PendingDegreeRepository pendingDegreeRepository;
    private final PendingCertificationRepository pendingCertificationRepository;

    private final CertificationRepository certificationRepository;
    private final DegreeRepository degreeRepository;

    private final PendingOwnedTrainingCourseRepository pendingOwnedTrainingCourseRepository;
    private final PendingAttendedTrainingCourseRepository pendingAttendedTrainingCourseRepository;
    private final PendingResearchProjectRepository pendingResearchProjectRepository;

    private final OwnedTrainingCourseRepository ownedTrainingCourseRepository;
    private final AttendedTrainingCourseRepository attendedTrainingCourseRepository;
    private final ResearchProjectRepository researchProjectRepository;


    @Transactional
    public LecturerDTO approveLecturer(LecturerReq req) {
        PendingLecturer pending = pendingLecturerRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));

        User user = pending.getUser();

        if (user.getLecturer() != null) {
            throw new IllegalStateException("User already has a Lecturer profile.");
        }

        user.setRole(Role.LECTURER);

        pending.setStatus(PendingStatus.APPROVED);
        pending.setUpdatedAt(LocalDateTime.now());

        if (pending.getPendingDegrees() != null) {
            pending.getPendingDegrees().forEach(degree -> {
                degree.setStatus(PendingStatus.APPROVED);
                degree.setReviewedAt(LocalDateTime.now());
                degree.setUpdatedAt(LocalDateTime.now());
            });
        }

        if (pending.getPendingCertifications() != null) {
            pending.getPendingCertifications().forEach(cert -> {
                cert.setStatus(PendingStatus.APPROVED);
                cert.setReviewedAt(LocalDateTime.now());
                cert.setUpdatedAt(LocalDateTime.now());
            });
        }

        Lecturer lecturer = Lecturer.builder()
                .citizenId(pending.getCitizenId())
                .user(user)
                .fullName(pending.getFullName())
                .dateOfBirth(pending.getDateOfBirth())
                .gender(pending.getGender())
                .bio(pending.getBio())
                .address(pending.getAddress())
                .avatarUrl("https://picsum.photos/200")
                .academicRank(pending.getAcademicRank())
                .specialization(pending.getSpecialization())
                .experienceYears(pending.getExperienceYears())
                .certifications(new ArrayList<>())
                .degrees(new ArrayList<>())
                .build();
        if (pending.getPendingDegrees() != null) {
            List<Degree> degrees = new ArrayList<>();
            for (PendingDegree p : pending.getPendingDegrees()) {
                p.setStatus(PendingStatus.APPROVED);
                p.setReviewedAt(LocalDateTime.now());
                p.setUpdatedAt(LocalDateTime.now());

                Degree degree = Degree.builder()
                        .referenceId(p.getReferenceId())
                        .lecturer(lecturer)
                        .name(p.getName())
                        .major(p.getMajor())
                        .institution(p.getInstitution())
                        .startYear(p.getStartYear())
                        .graduationYear(p.getGraduationYear())
                        .level(p.getLevel())
                        .url(p.getUrl())
                        .description(p.getDescription())
                        .build();

                degreeRepository.save(degree); // Lưu để có ID
                p.setOriginalId(degree.getId()); // Gán ID mới vào originalId

                degrees.add(degree);
            }
            lecturer.setDegrees(degrees);

        }
        if (pending.getPendingCertifications() != null) {
            List<Certification> certifications = new ArrayList<>();
            for (PendingCertification p : pending.getPendingCertifications()) {
                p.setStatus(PendingStatus.APPROVED);
                p.setReviewedAt(LocalDateTime.now());
                p.setUpdatedAt(LocalDateTime.now());

                Certification cert = Certification.builder()
                        .referenceId(p.getReferenceId())
                        .lecturer(lecturer)
                        .name(p.getName())
                        .issuedBy(p.getIssuedBy())
                        .issueDate(p.getIssueDate())
                        .expiryDate(p.getExpiryDate())
                        .certificateUrl(p.getCertificateUrl())
                        .level(p.getLevel())
                        .description(p.getDescription())
                        .build();

                certificationRepository.save(cert); // Lưu để sinh ID
                p.setOriginalId(cert.getId()); // Gán ID vào pending

                certifications.add(cert);
            }
            lecturer.setCertifications(certifications);

        }
        pendingLecturerRepository.save(pending);
        // Lưu Lecturer
        lecturerRepository.save(lecturer);

        return LecturerMapper.toLecturerDTO(lecturer);
    }

    @Transactional
    public LecturerCourseDTO getLecturerCourses(Integer lecturerId) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // ----- 1. OwnedTrainingCourse -----
        List<OwnedTrainingCourseDTO> ownedDTOs = lecturer.getOwnedTrainingCourses().stream()
                .map(c -> OwnedTrainingCourseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .topic(c.getTopic())
                        .courseType(c.getCourseType())
                        .scale(c.getScale())
                        .startDate(c.getStartDate())
                        .endDate(c.getEndDate())
                        .numberOfHour(c.getNumberOfHour())
                        .location(c.getLocation())
                        .status(c.getStatus())
                        .description(c.getDescription())
                        .courseUrl(c.getCourseUrl())
                        .build())
                .toList();

        // ----- 2. AttendedTrainingCourse -----
        List<AttendedTrainingCourseDTO> attendedDTOs = lecturer.getAttendedTrainingCourses().stream()
                .map(c -> AttendedTrainingCourseDTO.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .topic(c.getTopic())
                        .organizer(c.getOrganizer())
                        .courseType(c.getCourseType())
                        .scale(c.getScale())
                        .startDate(c.getStartDate())
                        .endDate(c.getEndDate())
                        .numberOfHour(c.getNumberOfHour())
                        .location(c.getLocation())
                        .description(c.getDescription())
                        .courseUrl(c.getCourseUrl())
                        .build())
                .toList();

        // ----- 3. ResearchProject -----
        List<ResearchProjectDTO> researchDTOs = lecturer.getResearchProjects().stream()
                .map(p -> ResearchProjectDTO.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .researchArea(p.getResearchArea())
                        .scale(p.getScale())
                        .startDate(p.getStartDate())
                        .endDate(p.getEndDate())
                        .foundingAmount(p.getFoundingAmount())
                        .foundingSource(p.getFoundingSource())
                        .projectType(p.getProjectType())
                        .roleInProject(p.getRoleInProject())
                        .publishedUrl(p.getPublishedUrl())
                        .status(p.getStatus())
                        .description(p.getDescription())
                        .build())
                .toList();

        // ----- 4. PendingOwnedTrainingCourse -----
        List<PendingOwnedTrainingCourseDTO> pendingOwnedDTOs = lecturer.getPendingOwnedTrainingCourses().stream()
                .map(p -> PendingOwnedTrainingCourseDTO.builder()
                        .id(p.getId())
                        .originalId(p.getOriginalId())
                        .title(p.getTitle())
                        .topic(p.getTopic())
                        .courseType(p.getCourseType())
                        .scale(p.getScale())
                        .startDate(p.getStartDate())
                        .endDate(p.getEndDate())
                        .numberOfHour(p.getNumberOfHour())
                        .location(p.getLocation())
                        .status(p.getStatus())
                        .description(p.getDescription())
                        .courseUrl(p.getCourseUrl())
                        .pendingStatus(p.getPendingStatus())
                        .reason(p.getReason())
                        .createdAt(p.getCreatedAt())
                        .updatedAt(p.getUpdatedAt())
                        .build())
                .toList();

        // ----- 5. PendingAttendedTrainingCourse -----
        List<PendingAttendedTrainingCourseDTO> pendingAttendedDTOs = lecturer.getPendingAttendedTrainingCourses().stream()
                .map(p -> PendingAttendedTrainingCourseDTO.builder()
                        .id(p.getId())
                        .originalId(p.getOriginalId())
                        .title(p.getTitle())
                        .topic(p.getTopic())
                        .organizer(p.getOrganizer())
                        .courseType(p.getCourseType())
                        .scale(p.getScale())
                        .startDate(p.getStartDate())
                        .endDate(p.getEndDate())
                        .numberOfHour(p.getNumberOfHour())
                        .location(p.getLocation())
                        .description(p.getDescription())
                        .courseUrl(p.getCourseUrl())
                        .pendingStatus(p.getPendingStatus())
                        .reason(p.getReason())
                        .createdAt(p.getCreatedAt())
                        .updatedAt(p.getUpdatedAt())
                        .build())
                .toList();

        // ----- 6. PendingResearchProject -----
        List<PendingResearchProjectDTO> pendingResearchDTOs = lecturer.getPendingResearchProjects().stream()
                .map(p -> PendingResearchProjectDTO.builder()
                        .id(p.getId())
                        .originalId(p.getOriginalId())
                        .title(p.getTitle())
                        .researchArea(p.getResearchArea())
                        .scale(p.getScale())
                        .startDate(p.getStartDate())
                        .endDate(p.getEndDate())
                        .foundingAmount(p.getFoundingAmount())
                        .foundingSource(p.getFoundingSource())
                        .projectType(p.getProjectType())
                        .roleInProject(p.getRoleInProject())
                        .publishedUrl(p.getPublishedUrl())
                        .status(p.getStatus())
                        .description(p.getDescription())
                        .pendingStatus(p.getPendingStatus())
                        .reason(p.getReason())
                        .createdAt(p.getCreatedAt())
                        .updatedAt(p.getUpdatedAt())
                        .build())
                .toList();

        return LecturerCourseDTO.builder()
                .citizenId(lecturer.getCitizenId())
                .fullName(lecturer.getFullName())
                .dateOfBirth(lecturer.getDateOfBirth())
                .gender(lecturer.getGender())
                .bio(lecturer.getBio())
                .address(lecturer.getAddress())
                .avatarUrl(lecturer.getAvatarUrl())
                .academicRank(lecturer.getAcademicRank())
                .specialization(lecturer.getSpecialization())
                .experienceYears(lecturer.getExperienceYears())
                .lecturerId(lecturer.getId())
                .ownedCourses(ownedDTOs)
                .attendedCourses(attendedDTOs)
                .researchProjects(researchDTOs)
                .pendingOwnedCourses(pendingOwnedDTOs)
                .pendingAttendedCourses(pendingAttendedDTOs)
                .pendingResearchProjects(pendingResearchDTOs)
                .build();
    }

    @Transactional
    public LecturerDTO approvePendingLecturerUpdate(LecturerReq req) {
        PendingLecturer pending = pendingLecturerRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));

        if(pending.getStatus()!=PendingStatus.PENDING){
            throw new AccessDeniedException("Only approve for PENDING status");
        }
        User user = pending.getUser();
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Lecturer does not exist for this user");
        }


        // Cập nhật các thông tin cơ bản
        lecturer.setFullName(pending.getFullName());
        lecturer.setDateOfBirth(pending.getDateOfBirth());
        lecturer.setGender(pending.getGender());
        lecturer.setBio(pending.getBio());
        lecturer.setAddress(pending.getAddress());
        lecturer.setAvatarUrl(pending.getAvatarUrl());
        lecturer.setAcademicRank(pending.getAcademicRank());
        lecturer.setSpecialization(pending.getSpecialization());
        lecturer.setExperienceYears(pending.getExperienceYears());

        lecturerRepository.save(lecturer);

        // Chỉ cập nhật trạng thái của PendingLecturer
        pending.setStatus(PendingStatus.APPROVED);
        pending.setUpdatedAt(LocalDateTime.now());

        return LecturerMapper.toLecturerDTO(lecturer);
    }

    @Transactional
    public void rejectPendingLecturerUpdate(ApplicationPendingReject req) {
        PendingLecturer pending = pendingLecturerRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));

        if (pending.getStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được từ chối bản cập nhật đang chờ duyệt");
        }

        pending.setStatus(PendingStatus.REJECTED);
        pending.setResponse(req.getReason());
        pending.setUpdatedAt(LocalDateTime.now());

        pendingLecturerRepository.save(pending);
    }
    @Transactional
    public void rejectPendingDegree(ApplicationPendingReject req) {
        PendingDegree pendingDegree = pendingDegreeRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));
        if(pendingDegree.getStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được từ chối bản cập nhật đang chờ duyệt");
        }
        pendingDegree.setStatus(PendingStatus.REJECTED);
        pendingDegree.setReason(req.getReason());
        pendingDegree.setUpdatedAt(LocalDateTime.now());

        pendingDegreeRepository.save(pendingDegree);
    }

    public void rejectPendingCertification(ApplicationPendingReject req) {
        PendingCertification pendingCertification = pendingCertificationRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));
        if(pendingCertification.getStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được từ chối bản cập nhật đang chờ duyệt");
        }
        pendingCertification.setStatus(PendingStatus.REJECTED);
        pendingCertification.setReason(req.getReason());
        pendingCertification.setUpdatedAt(LocalDateTime.now());

        pendingCertificationRepository.save(pendingCertification);
    }

    @Transactional
    public CertificationDTO approvePendingCertification(Integer pendingCertId) {
        PendingCertification pending = pendingCertificationRepository.findById(pendingCertId)
                .orElseThrow(() -> new RuntimeException("PendingCertification not found"));

        if (pending.getStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được duyệt chứng chỉ ở trạng thái PENDING");
        }

        Lecturer lecturer = pending.getPendingLecturer().getUser().getLecturer();
        if (lecturer == null) {
            throw new RuntimeException("Không tìm thấy Lecturer tương ứng với chứng chỉ đang chờ");
        }

        Certification cert;

        if (pending.getOriginalId() != null) {
            // Cập nhật Certification hiện có
            cert = certificationRepository.findById(pending.getOriginalId())
                    .orElseThrow(() -> new RuntimeException("Original Certification not found"));

            cert.setReferenceId(pending.getReferenceId());
            cert.setName(pending.getName());
            cert.setIssuedBy(pending.getIssuedBy());
            cert.setIssueDate(pending.getIssueDate());
            cert.setExpiryDate(pending.getExpiryDate());
            cert.setCertificateUrl(pending.getCertificateUrl());
            cert.setLevel(pending.getLevel());
            cert.setDescription(pending.getDescription());
        } else {
            // Tạo mới Certification
            cert = Certification.builder()
                    .referenceId(pending.getReferenceId())
                    .name(pending.getName())
                    .issuedBy(pending.getIssuedBy())
                    .issueDate(pending.getIssueDate())
                    .expiryDate(pending.getExpiryDate())
                    .certificateUrl(pending.getCertificateUrl())
                    .level(pending.getLevel())
                    .description(pending.getDescription())
                    .lecturer(lecturer)
                    .build();

            cert = certificationRepository.save(cert);
            pending.setOriginalId(cert.getId()); // Cập nhật lại originalId sau khi tạo mới
        }

        // Cập nhật trạng thái pending
        pending.setStatus(PendingStatus.APPROVED);
        pending.setReviewedAt(LocalDateTime.now());
        pendingCertificationRepository.save(pending);

        return LecturerMapper.toCertificationDTO(cert);
    }

    @Transactional
    public DegreeDTO approvePendingDegree(Integer pendingDegreeId) {
        PendingDegree pending = pendingDegreeRepository.findById(pendingDegreeId)
                .orElseThrow(() -> new RuntimeException("PendingDegree not found"));

        if (pending.getStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được duyệt bằng cấp ở trạng thái PENDING");
        }

        Lecturer lecturer = pending.getPendingLecturer().getUser().getLecturer();
        if (lecturer == null) {
            throw new RuntimeException("Không tìm thấy Lecturer tương ứng");
        }

        Degree degree;

        if (pending.getOriginalId() != null) {
            // Cập nhật Degree hiện có
            degree = degreeRepository.findById(pending.getOriginalId())
                    .orElseThrow(() -> new RuntimeException("Original Degree not found"));

            degree.setReferenceId(pending.getReferenceId());
            degree.setName(pending.getName());
            degree.setMajor(pending.getMajor());
            degree.setInstitution(pending.getInstitution());
            degree.setStartYear(pending.getStartYear());
            degree.setGraduationYear(pending.getGraduationYear());
            degree.setLevel(pending.getLevel());
            degree.setUrl(pending.getUrl());
            degree.setDescription(pending.getDescription());

        } else {
            // Tạo mới Degree
            degree = Degree.builder()
                    .referenceId(pending.getReferenceId())
                    .name(pending.getName())
                    .major(pending.getMajor())
                    .institution(pending.getInstitution())
                    .startYear(pending.getStartYear())
                    .graduationYear(pending.getGraduationYear())
                    .level(pending.getLevel())
                    .url(pending.getUrl())
                    .description(pending.getDescription())
                    .lecturer(lecturer)
                    .build();

            degree = degreeRepository.save(degree);
            pending.setOriginalId(degree.getId()); // gán ID mới vào originalId
        }

        // Cập nhật trạng thái pending
        pending.setStatus(PendingStatus.APPROVED);
        pending.setReason("");
        pending.setReviewedAt(LocalDateTime.now());
        pendingDegreeRepository.save(pending);

        return LecturerMapper.toDegreeDTO(degree);
    }

    @Transactional
    public void rejectPendingLecturer(RejectPendingLecturerRequest req) {
        PendingLecturer pending = pendingLecturerRepository.findById(req.getPendingLecturerId())
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));

        if (pending.getStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể từ chối khi đang ở trạng thái PENDING");
        }

        // Gán trạng thái cho Lecturer
        pending.setStatus(PendingStatus.REJECTED);
        pending.setResponse(req.getResponse());
        pending.setUpdatedAt(LocalDateTime.now());

        // Từ chối từng Degree nếu có
        if (req.getRejectedDegrees() != null) {
            for (var degreeReject : req.getRejectedDegrees()) {
                pending.getPendingDegrees().stream()
                        .filter(d -> d.getId().equals(degreeReject.getId()))
                        .findFirst()
                        .ifPresent(d -> {
                            d.setStatus(PendingStatus.REJECTED);
                            d.setReason(degreeReject.getReason());
                            d.setReviewedAt(LocalDateTime.now());
                            d.setUpdatedAt(LocalDateTime.now());
                        });
            }
        }

        // Từ chối từng Certification nếu có
        if (req.getRejectedCertifications() != null) {
            for (var certReject : req.getRejectedCertifications()) {
                pending.getPendingCertifications().stream()
                        .filter(c -> c.getId().equals(certReject.getId()))
                        .findFirst()
                        .ifPresent(c -> {
                            c.setStatus(PendingStatus.REJECTED);
                            c.setReason(certReject.getReason());
                            c.setReviewedAt(LocalDateTime.now());
                            c.setUpdatedAt(LocalDateTime.now());
                        });
            }
        }

        pendingLecturerRepository.save(pending);
    }

    @Transactional
    public OwnedTrainingCourseDTO approveOwnedCourse(Integer id) {
        PendingOwnedTrainingCourse pending = pendingOwnedTrainingCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PendingOwnedTrainingCourse not found"));

        if (pending.getPendingStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được duyệt khóa học ở trạng thái PENDING");
        }

        Lecturer lecturer = pending.getLecturer();
        if (lecturer == null) {
            throw new RuntimeException("Không tìm thấy Lecturer tương ứng");
        }

        OwnedTrainingCourse course;

        if (pending.getOriginalId() != null) {
            course = ownedTrainingCourseRepository.findById(pending.getOriginalId())
                    .orElseThrow(() -> new RuntimeException("Original OwnedTrainingCourse not found"));

            course.setTitle(pending.getTitle());
            course.setTopic(pending.getTopic());
            course.setCourseType(pending.getCourseType());
            course.setScale(pending.getScale());
            course.setStartDate(pending.getStartDate());
            course.setEndDate(pending.getEndDate());
            course.setNumberOfHour(pending.getNumberOfHour());
            course.setLocation(pending.getLocation());
            course.setStatus(pending.getStatus());
            course.setDescription(pending.getDescription());
            course.setCourseUrl(pending.getCourseUrl());
        } else {
            course = OwnedTrainingCourse.builder()
                    .title(pending.getTitle())
                    .topic(pending.getTopic())
                    .courseType(pending.getCourseType())
                    .scale(pending.getScale())
                    .startDate(pending.getStartDate())
                    .endDate(pending.getEndDate())
                    .numberOfHour(pending.getNumberOfHour())
                    .location(pending.getLocation())
                    .status(pending.getStatus())
                    .description(pending.getDescription())
                    .courseUrl(pending.getCourseUrl())
                    .lecturer(lecturer)
                    .build();

            course = ownedTrainingCourseRepository.save(course);
            pending.setOriginalId(course.getId());
        }

        // Cập nhật trạng thái
        pending.setPendingStatus(PendingStatus.APPROVED);
        pending.setUpdatedAt(LocalDateTime.now());
        pending.setReason("");
        pendingOwnedTrainingCourseRepository.save(pending);
        return LecturerMapper.toOwnedTrainingCourseDTO(course);
    }
    @Transactional
    public AttendedTrainingCourseDTO approveAttendedCourse(Integer id) {
        PendingAttendedTrainingCourse pending = pendingAttendedTrainingCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PendingAttendedTrainingCourse not found"));

        if (pending.getPendingStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được duyệt khóa học ở trạng thái PENDING");
        }

        Lecturer lecturer = pending.getLecturer();
        if (lecturer == null) {
            throw new RuntimeException("Không tìm thấy Lecturer tương ứng");
        }

        AttendedTrainingCourse course;

        if (pending.getOriginalId() != null) {
            course = attendedTrainingCourseRepository.findById(pending.getOriginalId())
                    .orElseThrow(() -> new RuntimeException("Original AttendedTrainingCourse not found"));

            course.setTitle(pending.getTitle());
            course.setTopic(pending.getTopic());
            course.setOrganizer(pending.getOrganizer());
            course.setCourseType(pending.getCourseType());
            course.setScale(pending.getScale());
            course.setStartDate(pending.getStartDate());
            course.setEndDate(pending.getEndDate());
            course.setNumberOfHour(pending.getNumberOfHour());
            course.setLocation(pending.getLocation());
            course.setDescription(pending.getDescription());
            course.setCourseUrl(pending.getCourseUrl());
        } else {
            course = AttendedTrainingCourse.builder()
                    .title(pending.getTitle())
                    .topic(pending.getTopic())
                    .organizer(pending.getOrganizer())
                    .courseType(pending.getCourseType())
                    .scale(pending.getScale())
                    .startDate(pending.getStartDate())
                    .endDate(pending.getEndDate())
                    .numberOfHour(pending.getNumberOfHour())
                    .location(pending.getLocation())
                    .description(pending.getDescription())
                    .courseUrl(pending.getCourseUrl())
                    .lecturer(lecturer)
                    .build();

            course = attendedTrainingCourseRepository.save(course);
            pending.setOriginalId(course.getId());
        }

        pending.setPendingStatus(PendingStatus.APPROVED);
        pending.setUpdatedAt(LocalDateTime.now());
        pending.setReason("");
        pendingAttendedTrainingCourseRepository.save(pending);

        return LecturerMapper.toAttendedTrainingCourseDTO(course);
    }

    @Transactional
    public ResearchProjectDTO approveResearchProject(Integer id) {
        PendingResearchProject pending = pendingResearchProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PendingResearchProject not found"));

        if (pending.getPendingStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được duyệt đề tài ở trạng thái PENDING");
        }

        Lecturer lecturer = pending.getLecturer();
        if (lecturer == null) {
            throw new RuntimeException("Không tìm thấy Lecturer tương ứng");
        }

        ResearchProject project;

        if (pending.getOriginalId() != null) {
            project = researchProjectRepository.findById(pending.getOriginalId())
                    .orElseThrow(() -> new RuntimeException("Original ResearchProject not found"));

            project.setTitle(pending.getTitle());
            project.setResearchArea(pending.getResearchArea());
            project.setScale(pending.getScale());
            project.setStartDate(pending.getStartDate());
            project.setEndDate(pending.getEndDate());
            project.setFoundingAmount(pending.getFoundingAmount());
            project.setFoundingSource(pending.getFoundingSource());
            project.setProjectType(pending.getProjectType());
            project.setRoleInProject(pending.getRoleInProject());
            project.setPublishedUrl(pending.getPublishedUrl());
            project.setStatus(pending.getStatus());
            project.setDescription(pending.getDescription());
        } else {
            project = ResearchProject.builder()
                    .title(pending.getTitle())
                    .researchArea(pending.getResearchArea())
                    .scale(pending.getScale())
                    .startDate(pending.getStartDate())
                    .endDate(pending.getEndDate())
                    .foundingAmount(pending.getFoundingAmount())
                    .foundingSource(pending.getFoundingSource())
                    .projectType(pending.getProjectType())
                    .roleInProject(pending.getRoleInProject())
                    .publishedUrl(pending.getPublishedUrl())
                    .status(pending.getStatus())
                    .description(pending.getDescription())
                    .lecturer(lecturer)
                    .build();

            project = researchProjectRepository.save(project);
            pending.setOriginalId(project.getId());
        }

        pending.setPendingStatus(PendingStatus.APPROVED);
        pending.setUpdatedAt(LocalDateTime.now());
        pending.setReason("");
        pendingResearchProjectRepository.save(pending);

        return LecturerMapper.toResearchProjectDTO(project);
    }

    @Transactional
    public void rejectOwnedCourse(ApplicationPendingReject req) {
        PendingOwnedTrainingCourse pendingOwnedTrainingCourse = pendingOwnedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));

        if (pendingOwnedTrainingCourse.getPendingStatus() != PendingStatus.PENDING) {
            throw new IllegalStateException("Chỉ được từ chối bản cập nhật đang chờ duyệt");
        }
        pendingOwnedTrainingCourse.setPendingStatus(PendingStatus.REJECTED);
        pendingOwnedTrainingCourse.setReason(req.getReason());
        pendingOwnedTrainingCourse.setUpdatedAt(LocalDateTime.now());
        pendingOwnedTrainingCourseRepository.save(pendingOwnedTrainingCourse);
//        PendingCertification pendingCertification = pendingCertificationRepository.findById(req.getId())
//                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));
//        if(pendingCertification.getStatus() != PendingStatus.PENDING) {
//            throw new IllegalStateException("Chỉ được từ chối bản cập nhật đang chờ duyệt");
//        }
//        pendingCertification.setStatus(PendingStatus.REJECTED);
//        pendingCertification.setReason(req.getReason());
//        pendingCertification.setUpdatedAt(LocalDateTime.now());
//
//        pendingCertificationRepository.save(pendingCertification);
    }
}
