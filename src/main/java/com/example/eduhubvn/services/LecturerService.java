package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.RejectReq;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.*;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.*;
import com.example.eduhubvn.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final DegreeRepository degreeRepository;
    private final CertificationRepository certificationRepository;
    private final AttendedTrainingCourseRepository attendedTrainingCourseRepository;
    private final DegreeUpdateRepository degreeUpdateRepository;
    private final CertificationUpdateRepository certificationUpdateRepository;
    private final AttendedTrainingCourseUpdateRepository attendedTrainingCourseUpdateRepository;
    private final OwnedTrainingCourseUpdateRepository ownedTrainingCourseUpdateRepository;
    private final LecturerUpdateRepository lecturerUpdateRequestRepository;
    private final OwnedTrainingCourseRepository ownedTrainingCourseRepository;
    private final ResearchProjectRepository researchProjectRepository;
    private final ResearchProjectUpdateRepository researchProjectUpdateRepository;

    private final LecturerMapper lecturerMapper;
    private final DegreeMapper degreeMapper;
    private final CertificationMapper certificationMapper;
    private final AttendedTrainingCourseMapper attendedTrainingCourseMapper;
    private final OwnedTrainingCourseMapper ownedTrainingCourseMapper;
    private final ResearchProjectMapper researchProjectMapper;

    /// Get

    @Transactional
    public List<LecturerPendingDTO> getPendingLecturerUpdates() {
        try {
            List<LecturerUpdate> pendingUpdates = lecturerUpdateRequestRepository.findByStatus(PendingStatus.PENDING);

            return pendingUpdates.stream()
                    .map(update -> {
                        Lecturer lecturer = update.getLecturer();
                        LecturerDTO lecturerDTO = lecturerMapper.toDTO(lecturer);
                        LecturerUpdateDTO lecturerUpdateDTO = lecturerMapper.toDTO(update);
                        return new LecturerPendingDTO(lecturerDTO, lecturerUpdateDTO);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Lecturer
    @Transactional
    public LecturerDTO createLecturer(LecturerReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if (user.getLecturer() != null) {
            throw new EntityNotFoundException("Đã có tài khoản");
        }
        try {
            Lecturer lecturer = lecturerMapper.toEntity(req);
            lecturer.setUser(user);
            lecturer.setStatus(PendingStatus.PENDING);
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();
            return lecturerMapper.toDTO(lecturer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public LecturerDTO updateLecturer(LecturerReq req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            lecturerMapper.updateEntityFromRequest(req, lecturer);
            lecturer.setStatus(PendingStatus.PENDING);
            lecturer.setAdminNote("");
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();
            return lecturerMapper.toDTO(lecturer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public LecturerDTO updateLecturerProfile(LecturerUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            Optional<LecturerUpdate> optional = lecturerUpdateRequestRepository.findByLecturer(lecturer);
            LecturerUpdate update;
            if (optional.isPresent()) {
                update = optional.get();
                lecturerMapper.updateUpdateFromRequest(req, update);
            } else {
                update = lecturerMapper.toUpdate(req);
                update.setLecturer(lecturer);
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            lecturerUpdateRequestRepository.save(update);
            lecturerUpdateRequestRepository.flush();
            return lecturerMapper.toDTOFromUpdate(update);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xử lý yêu cầu cập nhật: " + e.getMessage(), e);
        }
    }

    /// Degree
    @Transactional
    public List<DegreeDTO> saveDegrees(List<DegreeReq> reqs, User user) {
        if (reqs == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            List<Degree> degrees = degreeMapper.toEntities(reqs);
            degrees.forEach(degree -> {
                degree.setLecturer(lecturer);
                degree.setStatus(PendingStatus.PENDING);
                degree.setAdminNote("");
            });
            List<Degree> degreeList = degreeRepository.saveAll(degrees);
            degreeRepository.flush();
            return degreeMapper.toDTOs(degreeList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public DegreeDTO updateDegreeFromUser(DegreeUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Request không được trống");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        Degree degree = degreeRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        try {
            degreeMapper.updateEntityFromReq(req, degree);
            degree.setStatus(PendingStatus.PENDING);
            degree.setAdminNote("");
            degreeRepository.save(degree);
            degreeRepository.flush();
            return degreeMapper.toDTO(degree);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public DegreeDTO editDegreeFromUser(DegreeUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Request không được trống");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        Degree degree = degreeRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        try {
            Optional<DegreeUpdate> optional = degreeUpdateRepository.findByDegree(degree);
            DegreeUpdate update;
            if (optional.isPresent()) {
                update = optional.get();
                degreeMapper.updateUpdateFromRequest(req, update);
            } else {
                update = degreeMapper.toUpdate(req);
                update.setDegree(degree);
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            DegreeUpdate savedUpdate = degreeUpdateRepository.saveAndFlush(update);
            return degreeMapper.toDTO(savedUpdate);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật bằng cấp: " + e.getMessage(), e);
        }
    }

    /// Certification
    @Transactional
    public List<CertificationDTO> saveCertification(List<CertificationReq> reqs, User user) {
        if (reqs == null) {
            throw new IllegalStateException("Request không được trống");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            List<Certification> certifications = certificationMapper.toEntities(reqs);
            certifications.forEach(certification -> {
                certification.setLecturer(lecturer);
                certification.setStatus(PendingStatus.PENDING);
                certification.setAdminNote("");
            });
            List<Certification> certificationList = certificationRepository.saveAll(certifications);
            certificationRepository.flush();
            return certificationMapper.toDTOs(certificationList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public CertificationDTO updateCertificationFromUser(CertificationUpdateReq req, User user) {
        if (req.getId() == null) {
            throw new IllegalStateException("Request không được trống");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        Certification certification = certificationRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        Certification saved = null;
        try {
            certificationMapper.updateEntityFromReq(req, certification);
            certification.setStatus(PendingStatus.PENDING);
            certification.setAdminNote("");
            saved = certificationRepository.save(certification);
            certificationRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return certificationMapper.toDTO(saved);
    }

    @Transactional
    public CertificationDTO editCertificationFromUser(CertificationUpdateReq req, User user) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        Certification certification = certificationRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy."));
        try {
            Optional<CertificationUpdate> optionalUpdate = certificationUpdateRepository.findByCertification(certification);
            CertificationUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                certificationMapper.updateUpdateFromRequest(req, update);
            } else {
                update = certificationMapper.toUpdate(req);
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            certificationUpdateRepository.saveAndFlush(update);
            return certificationMapper.toDTO(update);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật chứng chỉ: " + e.getMessage(), e);
        }
    }

    /// Attended Course
    @Transactional
    public AttendedTrainingCourseDTO createAttendedCourse(AttendedTrainingCourseReq req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        AttendedTrainingCourse course = null;
        try {
            course = attendedTrainingCourseMapper.toEntity(req);
            course.setLecturer(lecturer);
            course.setStatus(PendingStatus.PENDING);
            course.setAdminNote("");
            attendedTrainingCourseRepository.save(course);
            attendedTrainingCourseRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return attendedTrainingCourseMapper.toDTO(course);
    }

    @Transactional
    public AttendedTrainingCourseDTO updateAttendedCourse(AttendedTrainingCourseUpdateReq req, User user) {
        if (req== null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        AttendedTrainingCourse course = attendedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy."));
        try {
            attendedTrainingCourseMapper.updateEntityFromReq(req, course);
            course.setStatus(PendingStatus.PENDING);
            course.setAdminNote("");
            attendedTrainingCourseRepository.save(course);
            return attendedTrainingCourseMapper.toDTO(course);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public AttendedTrainingCourseDTO editAttendedCourse(AttendedTrainingCourseUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        AttendedTrainingCourse course = attendedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy."));
        try {
            Optional<AttendedTrainingCourseUpdate> optionalUpdate =
                    attendedTrainingCourseUpdateRepository.findByAttendedTrainingCourse(course);
            AttendedTrainingCourseUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                attendedTrainingCourseMapper.updateUpdateFromRequest(req, update);
            } else {
                update = attendedTrainingCourseMapper.toUpdate(req);
                update.setAttendedTrainingCourse(course);
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            attendedTrainingCourseUpdateRepository.saveAndFlush(update);
            return attendedTrainingCourseMapper.toDTO(course);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật khóa học đã tham gia: " + e.getMessage(), e);
        }
    }

    /// Owned Course
    @Transactional
    public OwnedTrainingCourseDTO createOwnedCourse(OwnedTrainingCourseReq req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            OwnedTrainingCourse course = ownedTrainingCourseMapper.toEntity(req);
            course.setLecturer(lecturer);
            course.setStatus(PendingStatus.PENDING);
            course.setAdminNote("");
            ownedTrainingCourseRepository.save(course);
            ownedTrainingCourseRepository.flush();
            return ownedTrainingCourseMapper.toDTO(course);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OwnedTrainingCourseDTO updateOwnedCourse(OwnedTrainingCourseUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        OwnedTrainingCourse course = ownedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy."));
        try {
            ownedTrainingCourseMapper.updateEntityFromRequest(req, course);
            course.setStatus(PendingStatus.PENDING);
            course.setAdminNote("");
            ownedTrainingCourseRepository.save(course);
            ownedTrainingCourseRepository.flush();
            return ownedTrainingCourseMapper.toDTO(course);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OwnedTrainingCourseDTO editOwnedCourse(OwnedTrainingCourseUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        OwnedTrainingCourse course = ownedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy."));
        Optional<OwnedTrainingCourseUpdate> optional = ownedTrainingCourseUpdateRepository.findByOwnedTrainingCourse(course);
        OwnedTrainingCourseUpdate update;
        if (optional.isPresent()) {
            update = optional.get();
            ownedTrainingCourseMapper.updateUpdateFromRequest(req, update);
        } else {
            update = ownedTrainingCourseMapper.toEntity(req);
            update.setOwnedTrainingCourse(course);
        }
        try {
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            ownedTrainingCourseUpdateRepository.save(update);
            ownedTrainingCourseUpdateRepository.flush();
            return ownedTrainingCourseMapper.toDTO(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Research Project
    @Transactional
    public ResearchProjectDTO createResearchProject(ResearchProjectReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            ResearchProject project = researchProjectMapper.toEntity(req);
            project.setLecturer(lecturer);
            project.setStatus(PendingStatus.PENDING);
            project.setAdminNote("");
            researchProjectRepository.save(project);
            researchProjectRepository.flush();
            return researchProjectMapper.toDTO(project);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResearchProjectDTO updateResearchProject(ResearchProjectUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        ResearchProject project = researchProjectRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy."));
        try {
            researchProjectMapper.updateEntityFromRequest(req, project);
            project.setStatus(PendingStatus.PENDING);
            project.setAdminNote("");
            researchProjectRepository.save(project);
            researchProjectRepository.flush();
            return researchProjectMapper.toDTO(project);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResearchProjectDTO editResearchProject(ResearchProjectUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        ResearchProject project = researchProjectRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy."));
        try {
            Optional<ResearchProjectUpdate> optional = researchProjectUpdateRepository.findByResearchProject(project);
            ResearchProjectUpdate update;
            if (optional.isPresent()) {
                update = optional.get();
                researchProjectMapper.updateUpdateFromRequest(req, update);
            } else {
                update = researchProjectMapper.toEntity(req);
                update.setResearchProject(project);
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            researchProjectUpdateRepository.save(update);
            researchProjectUpdateRepository.flush();
            return researchProjectMapper.toDTO(project);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<LecturerCreateDTO> getPendingLecturerCreate() {
        try {
            List<Lecturer> pending = lecturerRepository.findByStatus(PendingStatus.PENDING);
            return pending.stream()
                    .map(lecturer -> LecturerCreateDTO.builder()
                            .lecturer(lecturerMapper.toDTO(lecturer))
                            //degree with status pending
                            .degrees(degreeMapper.toDTOs(degreeRepository.findByLecturerAndStatus(lecturer, PendingStatus.PENDING)))
                            .certificates(certificationMapper.toDTOs(certificationRepository.findByLecturerAndStatus(lecturer, PendingStatus.PENDING)))
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public DegreeDTO createDegreeFromUser(DegreeReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            Degree degree = degreeMapper.toEntity(req);
            degree.setLecturer(lecturer);
            degree.setStatus(PendingStatus.PENDING);
            degree.setAdminNote("");
            degreeRepository.save(degree);
            degreeRepository.flush();
            return degreeMapper.toDTO(degree);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public DegreeDTO updateDegree(DegreeUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        Degree degree = degreeRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        try {
            degreeMapper.updateEntityFromReq(req, degree);
            degree.setStatus(PendingStatus.PENDING);
            degree.setAdminNote("");
            degreeRepository.save(degree);
            degreeRepository.flush();
            return degreeMapper.toDTO(degree);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public CertificationDTO createCertificationFromUser(CertificationReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            Certification certification = certificationMapper.toEntity(req);
            certification.setLecturer(lecturer);
            certification.setStatus(PendingStatus.PENDING);
            certification.setAdminNote("");
            certificationRepository.save(certification);
            certificationRepository.flush();
            return certificationMapper.toDTO(certification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
