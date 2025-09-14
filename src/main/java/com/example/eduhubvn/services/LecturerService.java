package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.BooleanRequest;
import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.MessageSocket;
import com.example.eduhubvn.dtos.MessageSocketType;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.*;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.*;
import com.example.eduhubvn.repositories.*;
import com.example.eduhubvn.ulti.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    private final SimpMessagingTemplate messagingTemplate;

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
        if (lecturerRepository.existsByCitizenId(req.getCitizenId())) {
            throw new IllegalArgumentException("Số CMND/CCCD đã tồn tại trong hệ thống.");
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
            LecturerDTO dto = lecturerMapper.toDTO(lecturer);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.CREATE_LECTURER, dto));
            return dto;
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
        if (user.getRole() == Role.LECTURER && lecturer.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Bạn không thể cập nhật thông tin khi đã được phê duyệt.");
        }
        try {
            lecturerMapper.updateEntityFromRequest(req, lecturer);
            lecturer.setStatus(PendingStatus.PENDING);
            lecturer.setAdminNote("");
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();
            LecturerDTO dto = lecturerMapper.toDTO(lecturer);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.UPDATE_LECTURER, dto));
            return dto;
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
        if (lecturerRepository.existsByCitizenIdAndIdNot(req.getCitizenId(), lecturer.getId())) {
            throw new IllegalArgumentException("Số CMND/CCCD đã tồn tại trong hệ thống.");
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
            LecturerDTO dto = lecturerMapper.toDTOFromUpdate(update);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.EDIT_LECTURER, dto));
            return dto;
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
            IdRequest id = new IdRequest(lecturer.getId());
            List<DegreeDTO> dtos = degreeMapper.toDTOs(degreeList);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.CREATE_DEGREE, id));
            return dtos;
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
    public DegreeDTO editDegree(DegreeUpdateReq req, User user) {
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
            IdRequest id = new IdRequest(lecturer.getId());
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.EDIT_DEGREE, id));
            return degreeMapper.toDTO(savedUpdate);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật bằng cấp: " + e.getMessage(), e);
        }
    }

    @Transactional
    public DegreeDTO deleteDegree(IdRequest req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Degree degree = degreeRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bằng cấp với ID: " + req.getId()));
        try {
            degreeRepository.delete(degree);
            degreeRepository.flush();
            IdRequest id = new IdRequest(degree.getLecturer().getId());
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.DELETE_DEGREE, id));
            return degreeMapper.toDTO(degree);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            List<CertificationDTO> dtos = certificationMapper.toDTOs(certificationList);
            IdRequest id = new IdRequest(lecturer.getId());
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.CREATE_CERTIFICATION, id));
            return dtos;
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
            Optional<CertificationUpdate> optionalUpdate = certificationUpdateRepository
                    .findByCertification(certification);
            CertificationUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                certificationMapper.updateUpdateFromRequest(req, update);
            } else {
                update = certificationMapper.toUpdate(req);
                update.setCertification(certification);
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            certificationUpdateRepository.saveAndFlush(update);
            CertificationDTO dto = certificationMapper.toDTO(certification);
            IdRequest id = new IdRequest(certification.getId());
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.EDIT_CERTIFICATION, id));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật chứng chỉ: " + e.getMessage(), e);
        }
    }

    @Transactional
    public CertificationDTO deleteCertification(IdRequest req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Certification certification = certificationRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chứng chỉ với ID: " + req.getId()));
        try {
            certificationRepository.delete(certification);
            certificationRepository.flush();
            CertificationDTO dto = certificationMapper.toDTO(certification);
            IdRequest id = new IdRequest(certification.getId());
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.DELETE_CERTIFICATION, id));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            IdRequest id = new IdRequest(course.getLecturer().getId());
            AttendedTrainingCourseDTO dto = attendedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.CREATE_ATTENDED_COURSE, id));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public AttendedTrainingCourseDTO updateAttendedCourse(AttendedTrainingCourseUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null && user.getRole() != Role.ADMIN) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        AttendedTrainingCourse course = attendedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy."));
        try {
            attendedTrainingCourseMapper.updateEntityFromReq(req, course);
            course.setStatus(PendingStatus.PENDING);
            course.setAdminNote("");
            attendedTrainingCourseRepository.save(course);

            IdRequest id = new IdRequest(course.getLecturer().getId());
            AttendedTrainingCourseDTO dto = attendedTrainingCourseMapper.toDTO(course);
            if (lecturer != null) {
                messagingTemplate.convertAndSend("/topic/ADMIN",
                        new MessageSocket(MessageSocketType.UPDATE_ATTENDED_COURSE, id));
                
            }
            return dto;
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
            Optional<AttendedTrainingCourseUpdate> optionalUpdate = attendedTrainingCourseUpdateRepository
                    .findByAttendedTrainingCourse(course);
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
            IdRequest id = new IdRequest(course.getLecturer().getId());
            AttendedTrainingCourseDTO dto = attendedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.EDIT_ATTENDED_COURSE, id));
            return dto;
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
            IdRequest id = new IdRequest(course.getLecturer().getId());
            OwnedTrainingCourseDTO dto = ownedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.CREATE_OWNED_COURSE, id));
            return dto;
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
        if (lecturer == null && user.getRole() != Role.ADMIN) {
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
            IdRequest id = new IdRequest(course.getLecturer().getId());
            OwnedTrainingCourseDTO dto = ownedTrainingCourseMapper.toDTO(course);
            if (lecturer != null) {
                messagingTemplate.convertAndSend("/topic/ADMIN",
                        new MessageSocket(MessageSocketType.UPDATE_OWNED_COURSE, id));
            }
            return dto;
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
        Optional<OwnedTrainingCourseUpdate> optional = ownedTrainingCourseUpdateRepository
                .findByOwnedTrainingCourse(course);
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
            IdRequest id = new IdRequest(update.getOwnedTrainingCourse().getLecturer().getId());
            OwnedTrainingCourseDTO dto = ownedTrainingCourseMapper.toDTO(update);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.EDIT_OWNED_COURSE, id));
            return dto;
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
            IdRequest id = new IdRequest(project.getLecturer().getId());
            ResearchProjectDTO dto = researchProjectMapper.toDTO(project);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.CREATE_RESEARCH_PROJECT, id));
            return dto;
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
        if (lecturer == null && user.getRole() != Role.ADMIN) {
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
            IdRequest id = new IdRequest(project.getLecturer().getId());
            ResearchProjectDTO dto = researchProjectMapper.toDTO(project);
            if (lecturer != null) {
                messagingTemplate.convertAndSend("/topic/ADMIN",
                        new MessageSocket(MessageSocketType.UPDATE_RESEARCH_PROJECT, id));
            }
            return dto;
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
            ResearchProjectDTO dto = researchProjectMapper.toDTO(project);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.EDIT_RESEARCH_PROJECT, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResearchProjectDTO deleteResearchProject(IdRequest req, User user) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        ResearchProject project = researchProjectRepository.findById(req.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Không tìm thấy dự án nghiên cứu với ID: " + req.getId()));
        try {
            researchProjectRepository.delete(project);
            researchProjectRepository.flush();
            IdRequest id = new IdRequest(project.getLecturer().getId());
            ResearchProjectDTO dto = researchProjectMapper.toDTO(project);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.DELETE_RESEARCH_PROJECT, id));
            return dto;
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
                            .lecturer(Mapper.mapToLecturerInfoDTO(lecturer))
                            // degree with status pending
                            .degrees(degreeMapper.toDTOs(degreeRepository.findByLecturer(lecturer)))
                            .certificates(certificationMapper.toDTOs(certificationRepository.findByLecturer(lecturer)))
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
        if (lecturer == null && user.getRole() != Role.ADMIN) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        Degree degree = degreeRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        if (degree.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Bạn không thể cập nhật thông tin khi đã được phê duyệt.");
        }
        try {
            degreeMapper.updateEntityFromReq(req, degree);
            degree.setStatus(PendingStatus.PENDING);
            degree.setAdminNote("");
            degreeRepository.save(degree);
            degreeRepository.flush();
            if (lecturer != null) {
                IdRequest id = new IdRequest(lecturer.getId());
                messagingTemplate.convertAndSend("/topic/ADMIN",
                        new MessageSocket(MessageSocketType.UPDATE_DEGREE, id));
            }

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

    @Transactional
    public PendingLecturerDTO getPendingLecturerProfile(User user) {
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            List<Degree> degrees = degreeRepository.findByLecturer(lecturer);
            List<Certification> certifications = certificationRepository.findByLecturer(lecturer);
            return PendingLecturerDTO.builder()
                    .lecturer(lecturerMapper.toDTO(lecturer))
                    .degrees(degreeMapper.toDTOs(degrees))
                    .certifications(certificationMapper.toDTOs(certifications))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public PendingLecturerDTO updatePendingLecturer(PendingLecturerDTO req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            if (user.getRole() == Role.LECTURER && lecturer.getStatus() == PendingStatus.APPROVED) {
                throw new IllegalStateException("Bạn không thể cập nhật thông tin khi đã được phê duyệt.");

            }
            lecturerMapper.updateEntityFromDTO(req.getLecturer(), lecturer);
            lecturer.setStatus(PendingStatus.PENDING);
            lecturer.setAdminNote("");
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();

            List<Degree> degrees = degreeMapper.toEntitiesFromDtos(req.getDegrees());
            degrees.forEach(degree -> {
                degree.setLecturer(lecturer);
                degree.setAdminNote("");
            });
            List<Degree> savedDegrees = degreeRepository.saveAll(degrees);
            degreeRepository.flush();

            List<Certification> certifications = certificationMapper.toEntitiesFromDtos(req.getCertifications());
            certifications.forEach(certification -> {
                certification.setLecturer(lecturer);
                certification.setAdminNote("");
            });
            List<Certification> savedCertifications = certificationRepository.saveAll(certifications);
            certificationRepository.flush();

            PendingLecturerDTO dto = PendingLecturerDTO.builder()
                    .lecturer(lecturerMapper.toDTO(lecturer))
                    .degrees(degreeMapper.toDTOs(savedDegrees))
                    .certifications(certificationMapper.toDTOs(savedCertifications))
                    .build();
            messagingTemplate.convertAndSend("/topic/ADMIN", new MessageSocket(MessageSocketType.UPDATE_LECTURER, dto));
            return dto;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public LecturerProfileDTO getLecturerProfile(UUID idRequest) {
        if (idRequest == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(idRequest)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên."));
        try {

            List<Degree> degrees = degreeRepository.findByLecturer(lecturer);
            List<Certification> certifications = certificationRepository.findByLecturer(lecturer);
            List<OwnedTrainingCourse> ownedCourses = ownedTrainingCourseRepository.findByLecturer(lecturer);
            List<AttendedTrainingCourse> attendedTrainingCourses = attendedTrainingCourseRepository
                    .findByLecturer(lecturer);
            List<ResearchProject> researchProjects = researchProjectRepository.findByLecturer(lecturer);

            List<DegreeUpdateDTO> degreeUpdateDTOs = degrees.stream().map(degree -> {
                return DegreeUpdateDTO.builder().lecturer(null).original(degreeMapper.toDTO(degree))
                        .update(degreeMapper.toDTO(degree.getDegreeUpdate()))
                        .build();
            }).collect(Collectors.toList());

            List<CertificationUpdateDTO> certificationUpdateDTOs = certifications.stream().map(certification -> {
                return CertificationUpdateDTO.builder().lecturer(null)
                        .original(certificationMapper.toDTO(certification))
                        .update(certificationMapper.toDTO(certification.getCertificationUpdate()))
                        .build();
            }).collect(Collectors.toList());

            List<OwnedCourseUpdateDTO> ownedCourseUpdateDTOs = ownedCourses.stream().map(ownedCourse -> {
                return OwnedCourseUpdateDTO.builder().lecturer(null)
                        .original(ownedTrainingCourseMapper.toDTO(ownedCourse))
                        .update(ownedTrainingCourseMapper.toDTO(ownedCourse.getOwnedTrainingCourseUpdate()))
                        .build();
            }).collect(Collectors.toList());

            List<AttendedCourseUpdateDTO> attendedCourseUpdateDTOs = attendedTrainingCourses.stream()
                    .map(attendedCourse -> {
                        return AttendedCourseUpdateDTO.builder().lecturer(null)
                                .original(attendedTrainingCourseMapper.toDTO(attendedCourse))
                                .update(attendedTrainingCourseMapper
                                        .toDTO(attendedCourse.getAttendedTrainingCourseUpdate()))
                                .build();
                    }).collect(Collectors.toList());

            List<ResearchProjectUpdateDTO> researchProjectUpdateDTOs = researchProjects.stream()
                    .map(researchProject -> {
                        return ResearchProjectUpdateDTO.builder().lecturer(null)
                                .original(researchProjectMapper.toDTO(researchProject))
                                .update(researchProjectMapper.toDTO(researchProject.getResearchProjectUpdate()))
                                .build();
                    }).collect(Collectors.toList());

            return LecturerProfileDTO.builder()
                    .lecturer(Mapper.mapToLecturerInfoDTO(lecturer))
                    .lecturerUpdate(lecturerMapper
                            .toDTOFromUpdate(lecturerUpdateRequestRepository.findByLecturer(lecturer).orElse(null)))
                    .degrees(degreeUpdateDTOs)
                    .certificates(certificationUpdateDTOs)
                    .ownedCourses(ownedCourseUpdateDTOs)
                    .attendedCourses(attendedCourseUpdateDTOs)
                    .researchProjects(researchProjectUpdateDTOs)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public CertificationDTO updateCertification(CertificationUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null && user.getRole() != Role.ADMIN) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }

        Certification certification = certificationRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        try {
            certificationMapper.updateEntityFromReq(req, certification);
            certification.setStatus(PendingStatus.PENDING);
            certification.setAdminNote("");
            certificationRepository.save(certification);
            certificationRepository.flush();
            CertificationDTO dto = certificationMapper.toDTO(certification);
            if (lecturer != null) {
                IdRequest id = new IdRequest(lecturer.getId());
                messagingTemplate.convertAndSend("/topic/ADMIN",
                        new MessageSocket(MessageSocketType.UPDATE_CERTIFICATION, id));
            }
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public LecturerDTO hiddenLecturerProfile(User user, BooleanRequest hidden) {
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            lecturer.setHidden(hidden.isValue());
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();
            return lecturerMapper.toDTO(lecturer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public OwnedTrainingCourseDTO deleteOwnedCourse(IdRequest req, User user) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        OwnedTrainingCourse course = ownedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + req.getId()));
        try {
            ownedTrainingCourseRepository.delete(course);
            ownedTrainingCourseRepository.flush();
            IdRequest id = new IdRequest(course.getLecturer().getId());
            OwnedTrainingCourseDTO dto = ownedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.DELETE_OWNED_COURSE, id));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AttendedTrainingCourseDTO deleteAttendedCourse(IdRequest req, User user) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Lecturer lecturer = user.getLecturer();
        if (lecturer == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        AttendedTrainingCourse course = attendedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + req.getId()));
        try {
            attendedTrainingCourseRepository.delete(course);
            attendedTrainingCourseRepository.flush();
            IdRequest id = new IdRequest(course.getLecturer().getId());
            AttendedTrainingCourseDTO dto = attendedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.DELETE_ATTENDED_COURSE, id));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
