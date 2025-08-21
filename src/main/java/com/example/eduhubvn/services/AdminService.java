package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.*;
import com.example.eduhubvn.dtos.course.*;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.*;
import com.example.eduhubvn.repositories.*;
import com.example.eduhubvn.ulti.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final LecturerRepository lecturerRepository;
    private final LecturerUpdateRepository lecturerUpdateRepository;
    private final PartnerOrganizationRepository partnerOrganizationRepository;
    private final PartnerOrganizationUpdateRepository partnerOrganizationUpdateRepository;
    private final EducationInstitutionRepository educationInstitutionRepository;
    private final EducationInstitutionUpdateRepository educationInstitutionUpdateRepository;
    private final CertificationRepository certificationRepository;
    private final CertificationUpdateRepository certificationUpdateRepository;
    private final DegreeRepository degreeRepository;
    private final DegreeUpdateRepository degreeUpdateRepository;
    private final AttendedTrainingCourseRepository attendedTrainingCourseRepository;
    private final AttendedTrainingCourseUpdateRepository attendedTrainingCourseUpdateRepository;
    private final OwnedTrainingCourseRepository ownedTrainingCourseRepository;
    private final OwnedTrainingCourseUpdateRepository ownedTrainingCourseUpdateRepository;
    private final ResearchProjectRepository researchProjectRepository;
    private final ResearchProjectUpdateRepository researchProjectUpdateRepository;
    private final CourseRepository courseRepository;

    private final LecturerMapper lecturerMapper;
    private final PartnerOrganizationMapper partnerOrganizationMapper;
    private final EducationInstitutionMapper educationInstitutionMapper;
    private final CertificationMapper certificationMapper;
    private final DegreeMapper degreeMapper;
    private final AttendedTrainingCourseMapper attendedTrainingCourseMapper;
    private final OwnedTrainingCourseMapper ownedTrainingCourseMapper;
    private final ResearchProjectMapper researchProjectMapper;
    private final CourseMapper courseMapper;
    private final CourseLecturerRepository courseLecturerRepository;

    private final SimpMessagingTemplate messagingTemplate;

    /// Get

    @Transactional
    public AllPendingUpdateDTO getAllPendingUpdates() {
        try {
            List<LecturerUpdate> lecturerUpdates = lecturerUpdateRepository.findByStatus(PendingStatus.PENDING);
            List<PartnerOrganizationUpdate> partnerUpdates = partnerOrganizationUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            List<EducationInstitutionUpdate> eduInsUpdates = educationInstitutionUpdateRepository
                    .findByStatus(PendingStatus.PENDING);

            List<LecturerPendingDTO> lecturerDTOs = lecturerUpdates.stream()
                    .map(update -> new LecturerPendingDTO(
                            lecturerMapper.toDTO(update.getLecturer()),
                            lecturerMapper.toDTO(update)))
                    .toList();
            List<PartnerOrganizationPendingDTO> partnerDTOs = partnerUpdates.stream()
                    .map(update -> new PartnerOrganizationPendingDTO(
                            partnerOrganizationMapper.toDTO(update.getPartnerOrganization()),
                            partnerOrganizationMapper.toDTO(update)))
                    .toList();
            List<EducationInstitutionPendingDTO> eduDTOs = eduInsUpdates.stream()
                    .map(update -> new EducationInstitutionPendingDTO(
                            educationInstitutionMapper.toDTO(update.getEducationInstitution()),
                            educationInstitutionMapper.toDTO(update)))
                    .toList();
            return AllPendingUpdateDTO.builder()
                    .lecturerUpdates(lecturerDTOs)
                    .partnerUpdates(partnerDTOs)
                    .educationInstitutionUpdates(eduDTOs)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public AllPendingEntityDTO getAllPendingEntities() {
        try {
            List<LecturerDTO> lecturers = lecturerRepository.findByStatus(PendingStatus.PENDING).stream()
                    .map(lecturerMapper::toDTO)
                    .toList();
            List<PartnerOrganizationDTO> partners = partnerOrganizationRepository.findByStatus(PendingStatus.PENDING)
                    .stream()
                    .map(partnerOrganizationMapper::toDTO)
                    .toList();
            List<EducationInstitutionDTO> institutions = educationInstitutionRepository
                    .findByStatus(PendingStatus.PENDING).stream()
                    .map(educationInstitutionMapper::toDTO)
                    .toList();
            return AllPendingEntityDTO.builder()
                    .lecturers(lecturers)
                    .partnerOrganizations(partners)
                    .educationInstitutions(institutions)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Lecturer
    @Transactional
    public LecturerDTO approveLecturer(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        // if (lecturer.getStatus() == PendingStatus.APPROVED) {
        // throw new IllegalStateException("Đã được phê duyệt trước đó.");
        // }
        try {
            lecturer.getUser().setRole(Role.LECTURER);
            lecturer.setStatus(PendingStatus.APPROVED);
            lecturer.setAdminNote("");
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();
            return lecturerMapper.toDTO(lecturer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public LecturerDTO rejectLecturer(RejectReq req) {
        if (req == null || req.getId() == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        try {
            lecturer.setStatus(PendingStatus.REJECTED);
            lecturer.setAdminNote(req.getAdminNote());
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();
            return lecturerMapper.toDTO(lecturer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public LecturerDTO approveLecturerUpdate(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        LecturerUpdate update = lecturerUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        try {
            Lecturer lecturer = update.getLecturer();
            lecturerMapper.updateEntityFromUpdate(update, lecturer);
            update.setStatus(PendingStatus.APPROVED);
            update.setAdminNote("");
            lecturer.setStatus(PendingStatus.APPROVED);
            lecturer.setAdminNote("");
            lecturerRepository.save(lecturer);
            lecturerUpdateRepository.save(update);
            lecturerRepository.flush();
            return lecturerMapper.toDTO(lecturer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public LecturerUpdateDTO rejectLecturerUpdate(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống");
        }
        LecturerUpdate update = lecturerUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            lecturerUpdateRepository.save(update);
            lecturerUpdateRepository.flush();
            return lecturerMapper.toDTO(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Education Institution
    @Transactional
    public EducationInstitutionDTO approveEduIns(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution educationInstitution = educationInstitutionRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        // if (educationInstitution.getStatus() == PendingStatus.APPROVED) {
        // throw new IllegalStateException("Đã được phê duyệt trước đó.");
        // }
        try {
            educationInstitution.getUser().setRole(Role.SCHOOL);
            educationInstitution.setStatus(PendingStatus.APPROVED);
            educationInstitution.setAdminNote("");
            educationInstitutionRepository.save(educationInstitution);
            educationInstitutionRepository.flush();
            return educationInstitutionMapper.toDTO(educationInstitution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EducationInstitutionDTO rejectInstitution(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution institution = educationInstitutionRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));

        try {
            institution.setStatus(PendingStatus.REJECTED);
            institution.setAdminNote(req.getAdminNote());
            educationInstitutionRepository.save(institution);
            educationInstitutionRepository.flush();
            return educationInstitutionMapper.toDTO(institution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EducationInstitutionDTO approveEduInsUpdate(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống");
        }
        EducationInstitutionUpdate update = educationInstitutionUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        // if (update.getStatus() == PendingStatus.APPROVED) {
        // throw new IllegalStateException("Đã được phê duyệt trước đó.");
        // }
        try {
            EducationInstitution educationInstitution = update.getEducationInstitution();
            educationInstitutionMapper.updateEntityFromUpdate(update, educationInstitution);
            update.setStatus(PendingStatus.APPROVED);
            update.setAdminNote("");
            educationInstitution.setStatus(PendingStatus.APPROVED);
            educationInstitution.setAdminNote("");
            educationInstitutionRepository.save(educationInstitution);
            educationInstitutionUpdateRepository.save(update);
            educationInstitutionRepository.flush();
            return educationInstitutionMapper.toDTO(educationInstitution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EducationInstitutionUpdateDTO rejectEduInsUpdate(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống");
        }
        EducationInstitutionUpdate update = educationInstitutionUpdateRepository
                .findByIdAndStatus(req.getId(), PendingStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));

        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            educationInstitutionUpdateRepository.save(update);
            educationInstitutionUpdateRepository.flush();
            return educationInstitutionMapper.toDTO(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Partner Organization
    @Transactional
    public PartnerOrganizationDTO approvePartner(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganization partnerOrganization = partnerOrganizationRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        // if (partnerOrganization.getStatus() == PendingStatus.APPROVED) {
        // throw new IllegalStateException("Đã được phê duyệt trước đó.");
        // }
        try {
            partnerOrganization.getUser().setRole(Role.ORGANIZATION);
            partnerOrganization.setStatus(PendingStatus.APPROVED);
            partnerOrganizationRepository.save(partnerOrganization);
            partnerOrganizationRepository.flush();
            return partnerOrganizationMapper.toDTO(partnerOrganization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public PartnerOrganizationDTO approvePartnerUpdate(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganizationUpdate update = partnerOrganizationUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        // if (update.getStatus() == PendingStatus.APPROVED) {
        // throw new IllegalStateException("Đã được phê duyệt trước đó.");
        // }
        try {
            PartnerOrganization partnerOrganization = update.getPartnerOrganization();
            partnerOrganizationMapper.updateEntityFromUpdate(update, partnerOrganization);
            update.setStatus(PendingStatus.APPROVED);
            update.setAdminNote("");
            partnerOrganization.setStatus(PendingStatus.APPROVED);
            partnerOrganization.setAdminNote("");
            partnerOrganizationRepository.save(partnerOrganization);
            partnerOrganizationUpdateRepository.save(update);
            partnerOrganizationRepository.flush();
            return partnerOrganizationMapper.toDTO(partnerOrganization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public PartnerOrganizationUpdateDTO rejectPartnerUpdate(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganizationUpdate update = partnerOrganizationUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));

        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            partnerOrganizationUpdateRepository.save(update);
            partnerOrganizationUpdateRepository.flush();
            return partnerOrganizationMapper.toDTO(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public PartnerOrganizationDTO rejectPartner(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganization organization = partnerOrganizationRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));

        try {
            organization.setStatus(PendingStatus.REJECTED);
            organization.setAdminNote(req.getAdminNote());
            partnerOrganizationRepository.save(organization);
            partnerOrganizationRepository.flush();
            return partnerOrganizationMapper.toDTO(organization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Degree

    @Transactional
    public DegreeDTO approveDegree(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Degree degree = degreeRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        try {
            degree.setStatus(PendingStatus.APPROVED);
            Degree saved = degreeRepository.save(degree);
            degreeRepository.flush();
            DegreeDTO dto = degreeMapper.toDTO(saved);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + degree.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_DEGREE, dto));
            return dto;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public DegreeDTO approveDegreeUpdate(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        DegreeUpdate update = degreeUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        try {
            Degree degree = update.getDegree();
            degreeMapper.updateEntityFromUpdate(update, degree);
            degree.setStatus(PendingStatus.APPROVED);
            degree.setAdminNote("");
            update.setStatus(PendingStatus.APPROVED);
            update.setAdminNote("");
            degreeRepository.save(degree);
            degreeUpdateRepository.save(update);
            degreeRepository.flush();
            DegreeDTO dto = degreeMapper.toDTO(degree);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + degree.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_DEGREE_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public DegreeDTO rejectDegree(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Degree degree = degreeRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            degree.setStatus(PendingStatus.REJECTED);
            degree.setAdminNote(req.getAdminNote());
            degreeRepository.save(degree);
            degreeRepository.flush();
            messagingTemplate.convertAndSend("/topic/LECTURER/" + degree.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_DEGREE, degreeMapper.toDTO(degree)));
            return degreeMapper.toDTO(degree);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public DegreeDTO rejectDegreeUpdate(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        DegreeUpdate update = degreeUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            degreeUpdateRepository.save(update);
            degreeUpdateRepository.flush();
            DegreeDTO dto = degreeMapper.toDTO(update);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + update.getDegree().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_DEGREE_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Certification
    @Transactional
    public CertificationDTO approveCertification(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Certification certification = certificationRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            certification.setStatus(PendingStatus.APPROVED);
            Certification saved = certificationRepository.save(certification);
            certificationRepository.flush();
            CertificationDTO dto = certificationMapper.toDTO(saved);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + certification.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_CERTIFICATION, dto));
            return dto;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public CertificationDTO approveCertificationUpdate(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        CertificationUpdate update = certificationUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        // if (update.getStatus() == PendingStatus.APPROVED) {
        // throw new IllegalStateException("Đã được phê duyệt trước đó.");
        // }
        try {
            Certification certification = update.getCertification();
            certificationMapper.updateEntityFromUpdate(update, certification);
            certification.setStatus(PendingStatus.APPROVED);
            certification.setAdminNote("");
            update.setStatus(PendingStatus.APPROVED);
            update.setAdminNote("");
            certificationRepository.save(certification);
            certificationUpdateRepository.save(update);
            certificationRepository.flush();
            CertificationDTO dto = certificationMapper.toDTO(certification);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + certification.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_CERTIFICATION_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public CertificationDTO rejectCertification(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        Certification certification = certificationRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            certification.setStatus(PendingStatus.REJECTED);
            certification.setAdminNote(req.getAdminNote());
            certificationRepository.save(certification);
            certificationRepository.flush();
            CertificationDTO dto = certificationMapper.toDTO(certification);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + certification.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_CERTIFICATION, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public CertificationDTO rejectEditCertification(RejectReq req) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        CertificationUpdate update = certificationUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            certificationUpdateRepository.saveAndFlush(update);
            CertificationDTO dto = certificationMapper.toDTO(update);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + update.getCertification().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_CERTIFICATION_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Attended course
    @Transactional
    public AttendedTrainingCourseDTO approveAttendedCourse(IdRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        AttendedTrainingCourse course = attendedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            course.setStatus(PendingStatus.APPROVED);
            course.setAdminNote("");
            attendedTrainingCourseRepository.saveAndFlush(course);
            AttendedTrainingCourseDTO dto = attendedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + course.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_ATTENDED_COURSE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public AttendedTrainingCourseDTO approveAttendedCourseUpdate(IdRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        AttendedTrainingCourseUpdate update = attendedTrainingCourseUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        // if (update.getStatus() == PendingStatus.APPROVED) {
        // throw new IllegalStateException("Đã được phê duyệt trước đó.");
        // }
        try {
            AttendedTrainingCourse original = update.getAttendedTrainingCourse();
            attendedTrainingCourseMapper.updateEntityFromUpdate(update, original);
            original.setStatus(PendingStatus.APPROVED);
            original.setAdminNote("");
            update.setStatus(PendingStatus.APPROVED);
            update.setAdminNote("");
            attendedTrainingCourseRepository.save(original);
            attendedTrainingCourseUpdateRepository.save(update);
            attendedTrainingCourseRepository.flush();
            AttendedTrainingCourseDTO dto = attendedTrainingCourseMapper.toDTO(original);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + original.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_ATTENDED_COURSE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public AttendedTrainingCourseDTO rejectAttendedCourse(RejectReq req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        AttendedTrainingCourse course = attendedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            course.setStatus(PendingStatus.REJECTED);
            course.setAdminNote(req.getAdminNote());
            attendedTrainingCourseRepository.saveAndFlush(course);
            AttendedTrainingCourseDTO dto = attendedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + course.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_ATTENDED_COURSE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public AttendedTrainingCourseDTO rejectAttendedCourseUpdate(RejectReq req) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        AttendedTrainingCourseUpdate update = attendedTrainingCourseUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            attendedTrainingCourseUpdateRepository.save(update);
            attendedTrainingCourseUpdateRepository.flush();
            AttendedTrainingCourseDTO dto = attendedTrainingCourseMapper.toDTO(update);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + update.getAttendedTrainingCourse().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_ATTENDED_COURSE_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Owned course
    @Transactional
    public OwnedTrainingCourseDTO approveOwnedCourse(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        OwnedTrainingCourse course = ownedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            course.setStatus(PendingStatus.APPROVED);
            course.setAdminNote("");
            ownedTrainingCourseRepository.save(course);
            ownedTrainingCourseRepository.flush();
            OwnedTrainingCourseDTO dto = ownedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + course.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_OWNED_COURSE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OwnedTrainingCourseDTO approveOwnedCourseUpdate(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        OwnedTrainingCourseUpdate update = ownedTrainingCourseUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        // if (update.getStatus() == PendingStatus.APPROVED) {
        // throw new IllegalStateException("Đã được phê duyệt trước đó.");
        // }
        try {
            OwnedTrainingCourse original = update.getOwnedTrainingCourse();
            ownedTrainingCourseMapper.updateEntityFromUpdate(update, original);
            original.setStatus(PendingStatus.APPROVED);
            original.setAdminNote("");
            update.setStatus(PendingStatus.APPROVED);
            update.setAdminNote("");
            ownedTrainingCourseRepository.save(original);
            ownedTrainingCourseUpdateRepository.save(update);
            ownedTrainingCourseRepository.flush();
            OwnedTrainingCourseDTO dto = ownedTrainingCourseMapper.toDTO(original);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + original.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_OWNED_COURSE_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OwnedTrainingCourseDTO rejectOwnedCourse(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        OwnedTrainingCourse course = ownedTrainingCourseRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            course.setStatus(PendingStatus.REJECTED);
            course.setAdminNote(req.getAdminNote());
            ownedTrainingCourseRepository.save(course);
            ownedTrainingCourseRepository.flush();
            OwnedTrainingCourseDTO dto = ownedTrainingCourseMapper.toDTO(course);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + course.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_OWNED_COURSE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public OwnedTrainingCourseDTO rejectOwnedCourseUpdate(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        OwnedTrainingCourseUpdate update = ownedTrainingCourseUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            ownedTrainingCourseUpdateRepository.save(update);
            ownedTrainingCourseUpdateRepository.flush();
            OwnedTrainingCourseDTO dto = ownedTrainingCourseMapper.toDTO(update);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + update.getOwnedTrainingCourse().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_OWNED_COURSE_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Research project
    @Transactional
    public ResearchProjectDTO approveResearchProject(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        ResearchProject project = researchProjectRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            project.setStatus(PendingStatus.APPROVED);
            project.setAdminNote("");
            researchProjectRepository.save(project);
            researchProjectRepository.flush();
            ResearchProjectDTO dto = researchProjectMapper.toDTO(project);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + project.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_RESEARCH_PROJECT, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResearchProjectDTO approveResearchProjectUpdate(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        ResearchProjectUpdate update = researchProjectUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));
        try {
            ResearchProject original = update.getResearchProject();
            researchProjectMapper.updateEntityFromUpdate(update, original);
            original.setStatus(PendingStatus.APPROVED);
            original.setAdminNote("");
            update.setStatus(PendingStatus.APPROVED);
            update.setAdminNote("");
            researchProjectRepository.save(original);
            researchProjectUpdateRepository.save(update);
            researchProjectRepository.flush();
            ResearchProjectDTO dto = researchProjectMapper.toDTO(original);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + original.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_RESEARCH_PROJECT_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResearchProjectDTO rejectResearchProject(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        ResearchProject project = researchProjectRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            project.setStatus(PendingStatus.REJECTED);
            project.setAdminNote(req.getAdminNote());
            researchProjectRepository.save(project);
            researchProjectRepository.flush();
            ResearchProjectDTO dto = researchProjectMapper.toDTO(project);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + project.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_RESEARCH_PROJECT, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResearchProjectDTO rejectResearchProjectUpdate(RejectReq req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        ResearchProjectUpdate project = researchProjectUpdateRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy"));

        try {
            project.setStatus(PendingStatus.REJECTED);
            project.setAdminNote(req.getAdminNote());
            researchProjectUpdateRepository.save(project);
            researchProjectUpdateRepository.flush();
            ResearchProjectDTO dto = researchProjectMapper.toDTO(project);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + project.getResearchProject().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_RESEARCH_PROJECT_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<DegreeUpdateDTO> getPendingDegreeUpdates() {
        try {
            List<DegreeUpdate> pendingUpdates = degreeUpdateRepository.findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getDegree() != null
                            && update.getDegree().getStatus() == PendingStatus.APPROVED)
                    .map(update -> DegreeUpdateDTO.builder()
                            .original(degreeMapper.toDTO(update.getDegree()))
                            .update(degreeMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getDegree().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending degree updates.", e);
        }
    }

    @Transactional
    public List<CertificationUpdateDTO> getPendingCertificationUpdates() {
        try {
            List<CertificationUpdate> pendingUpdates = certificationUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getCertification() != null
                            && update.getCertification().getStatus() == PendingStatus.APPROVED)
                    .map(update -> CertificationUpdateDTO.builder()
                            .original(certificationMapper.toDTO(update.getCertification()))
                            .update(certificationMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getCertification().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending degree updates.", e);
        }
    }

    @Transactional
    public List<AttendedCourseUpdateDTO> getPendingAttendedCourseUpdates() {
        try {
            List<AttendedTrainingCourseUpdate> pendingUpdates = attendedTrainingCourseUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getAttendedTrainingCourse() != null
                            && update.getAttendedTrainingCourse().getStatus() == PendingStatus.APPROVED)
                    .map(update -> AttendedCourseUpdateDTO.builder()
                            .original(attendedTrainingCourseMapper.toDTO(update.getAttendedTrainingCourse()))
                            .update(attendedTrainingCourseMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getAttendedTrainingCourse().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending degree updates.", e);
        }
    }

    @Transactional
    public List<OwnedCourseUpdateDTO> getPendingOwnedCourseUpdates() {
        try {
            List<OwnedTrainingCourseUpdate> pendingUpdates = ownedTrainingCourseUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getOwnedTrainingCourse() != null
                            && update.getOwnedTrainingCourse().getStatus() == PendingStatus.APPROVED)
                    .map(update -> OwnedCourseUpdateDTO.builder()
                            .original(ownedTrainingCourseMapper.toDTO(update.getOwnedTrainingCourse()))
                            .update(ownedTrainingCourseMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getOwnedTrainingCourse().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending updates.", e);
        }
    }

    @Transactional
    public List<ResearchProjectUpdateDTO> getPendingResearchProjectUpdates() {
        try {
            List<ResearchProjectUpdate> pendingUpdates = researchProjectUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getResearchProject() != null
                            && update.getResearchProject().getStatus() == PendingStatus.APPROVED)
                    .map(update -> ResearchProjectUpdateDTO.builder()
                            .original(researchProjectMapper.toDTO(update.getResearchProject()))
                            .update(researchProjectMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getResearchProject().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending degree updates.", e);
        }
    }

    @Transactional
    public List<DegreePendingCreateDTO> getPendingDegreeCreate() {
        try {
            List<Degree> pending = degreeRepository.findByStatus(PendingStatus.PENDING);
            return pending.stream()
                    .filter(d -> d.getLecturer() != null && d.getLecturer().getStatus() == PendingStatus.APPROVED)
                    .map(d -> DegreePendingCreateDTO.builder()
                            .degree(degreeMapper.toDTO(d))
                            .lecturer(lecturerMapper.toDTO(d.getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending degree creates.", e);
        }
    }

    @Transactional
    public List<LecturerInfoDTO> getAllLecturers() {
        try {
            List<Lecturer> lecturers = lecturerRepository.findAll();
            return lecturers.stream()
                    .map(this::mapToLecturerInfoDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all lecturers.", e);
        }
    }

    private LecturerInfoDTO mapToLecturerInfoDTO(Lecturer lecturer) {
        return LecturerInfoDTO.builder()
                .id(lecturer.getId())
                .citizenId(lecturer.getCitizenId())
                .email(lecturer.getUser() != null ? lecturer.getUser().getEmail() : null)
                .phoneNumber(lecturer.getPhoneNumber())
                .fullName(lecturer.getFullName())
                .dateOfBirth(lecturer.getDateOfBirth())
                .gender(lecturer.getGender())
                .bio(lecturer.getBio())
                .address(lecturer.getAddress())
                .avatarUrl(lecturer.getAvatarUrl())
                .academicRank(lecturer.getAcademicRank())
                .specialization(lecturer.getSpecialization())
                .experienceYears(lecturer.getExperienceYears())
                .jobField(lecturer.getJobField())
                .hidden(lecturer.isHidden())
                .adminNote(lecturer.getAdminNote())
                .status(lecturer.getStatus())
                .createdAt(lecturer.getCreatedAt())
                .updatedAt(lecturer.getUpdatedAt())
                .build();
    }

    @Transactional
    public LecturerDTO updateLecturer(LecturerUpdateDTO req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Lecturer lecturer = lecturerRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        if (lecturerRepository.existsByCitizenIdAndIdNot(req.getCitizenId(), lecturer.getId())) {
            throw new IllegalArgumentException("Số CMND/CCCD đã tồn tại trong hệ thống.");
        }
        try {
            lecturerMapper.updateEntityFromUpdate(req, lecturer);
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();
            return lecturerMapper.toDTO(lecturer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<EducationInstitutionDTO> getAllInstitutions() {
        try {
            List<EducationInstitution> institutions = educationInstitutionRepository.findAll();
            return institutions.stream()
                    // .filter(institution -> institution.getStatus() == PendingStatus.APPROVED)
                    .map(educationInstitutionMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all institutions.", e);
        }
    }

    @Transactional
    public EducationInstitutionDTO updateInstitution(EducationInstitutionUpdateDTO req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        if (educationInstitutionRepository.existsByBusinessRegistrationNumberAndIdNot(
                req.getBusinessRegistrationNumber(), req.getId())) {
            throw new IllegalArgumentException("Mã số đăng ký kinh doanh đã tồn tại.");
        }
        EducationInstitution institution = educationInstitutionRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        try {
            educationInstitutionMapper.updateEntityFromUpdate(req, institution);
            educationInstitutionRepository.save(institution);
            educationInstitutionRepository.flush();
            return educationInstitutionMapper.toDTO(institution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PartnerOrganizationDTO updatePartner(PartnerOrganizationUpdateDTO req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        PartnerOrganization organization = partnerOrganizationRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        if (partnerOrganizationRepository.existsByBusinessRegistrationNumberAndIdNot(
                req.getBusinessRegistrationNumber(), req.getId())) {
            throw new IllegalArgumentException("Mã số đăng ký kinh doanh đã tồn tại.");

        }
        try {
            partnerOrganizationMapper.updateEntityFromUpdate(req, organization);
            partnerOrganizationRepository.save(organization);
            partnerOrganizationRepository.flush();
            return partnerOrganizationMapper.toDTO(organization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<PartnerOrganizationDTO> getAllPartners() {
        try {
            List<PartnerOrganization> partners = partnerOrganizationRepository.findAll();
            return partners.stream()
                    // .filter(partner -> partner.getStatus() == PendingStatus.APPROVED)
                    .map(partnerOrganizationMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all partners.", e);
        }
    }

    @Transactional
    public LecturerAllInfoDTO getLecturerProfile(UUID lecturerId, User user) {
        try {
            Lecturer lecturer = lecturerRepository.findById(lecturerId)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy giảng viên với ID: " + lecturerId));

            boolean canViewSensitiveInfo = user.getRole().equals(Role.ADMIN) || !lecturer.isHidden()
                    || user.getId().equals(lecturer.getUser().getId());

            // LecturerInfoDTO lecturerInfo = Mapper.mapToLecturerInfoDTO(lecturer);

            LecturerInfoDTO lecturerInfo = LecturerInfoDTO.builder()
                    .id(lecturer.getId())
                    .citizenId(canViewSensitiveInfo ? lecturer.getCitizenId() : "***")
                    .email(canViewSensitiveInfo
                            ? (lecturer.getUser() != null ? lecturer.getUser().getEmail() : null)
                            : "***")
                    .phoneNumber(canViewSensitiveInfo ? lecturer.getPhoneNumber() : "***")
                    .fullName(lecturer.getFullName())
                    .dateOfBirth(canViewSensitiveInfo ? lecturer.getDateOfBirth() : null)
                    .gender(lecturer.getGender())
                    .bio(canViewSensitiveInfo ? lecturer.getBio() : "***")
                    .address(canViewSensitiveInfo ? lecturer.getAddress() : "***")
                    .avatarUrl(lecturer.getAvatarUrl())
                    .academicRank(lecturer.getAcademicRank())
                    .specialization(lecturer.getSpecialization())
                    .experienceYears(lecturer.getExperienceYears())
                    .jobField(lecturer.getJobField())
                    .hidden(lecturer.isHidden())
                    .adminNote(lecturer.getAdminNote())
                    .status(lecturer.getStatus())
                    .createdAt(lecturer.getCreatedAt())
                    .updatedAt(lecturer.getUpdatedAt())
                    .build();

            List<CertificationDTO> certifications = lecturer.getCertifications().stream()
                    .map(certificationMapper::toDTO)
                    .toList();
            List<DegreeDTO> degrees = lecturer.getDegrees().stream()
                    .map(degreeMapper::toDTO)
                    .toList();
            List<AttendedTrainingCourseDTO> attendedCourses = lecturer.getAttendedTrainingCourses().stream()
                    .map(attendedTrainingCourseMapper::toDTO)
                    .toList();
            List<OwnedTrainingCourseDTO> ownedCourses = lecturer.getOwnedTrainingCourses().stream()
                    .map(ownedTrainingCourseMapper::toDTO)
                    .toList();
            List<ResearchProjectDTO> researchProjects = lecturer.getResearchProjects().stream()
                    .map(researchProjectMapper::toDTO)
                    .toList();
            return LecturerAllInfoDTO.builder()
                    .lecturer(lecturerInfo)
                    .certifications(certifications)
                    .degrees(degrees)
                    .attendedTrainingCourses(attendedCourses)
                    .ownedTrainingCourses(ownedCourses)
                    .researchProjects(researchProjects)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public List<RequestFromLecturer<?>> getLecturerRequests() {
        try {
            List<RequestFromLecturer<?>> requests = new ArrayList<>();

            // Add Degree requests
            List<Degree> degrees = degreeRepository.findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            for (Degree degree : degrees) {
                requests.add(RequestFromLecturer.<DegreeDTO>builder()
                        .content(degreeMapper.toDTO(degree))
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(degree.getLecturer())) 
                        .type(RequestLecturerType.BC)
                        .label(RequestLabel.Create)
                        .date(degree.getUpdatedAt())
                        .build());
            }

            // Add Certification requests
            List<Certification> certifications = certificationRepository
                    .findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            for (Certification certification : certifications) {
                requests.add(RequestFromLecturer.<CertificationDTO>builder()
                        .content(certificationMapper.toDTO(certification))
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(certification.getLecturer()))
                        .type(RequestLecturerType.CC)
                        .label(RequestLabel.Create)
                        .date(certification.getUpdatedAt())
                        .build());
            }

            // Add AttendedTrainingCourse requests
            List<AttendedTrainingCourse> attendedCourses = attendedTrainingCourseRepository
                    .findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            System.out.println("Found " + attendedCourses.size() + " pending attended training course requests.");
            for (AttendedTrainingCourse course : attendedCourses) {
                requests.add(RequestFromLecturer.<AttendedTrainingCourseDTO>builder()
                        .content(attendedTrainingCourseMapper.toDTO(course))
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(course.getLecturer()))
                        .type(RequestLecturerType.AC)
                        .label(RequestLabel.Create)
                        .date(course.getUpdatedAt())
                        .build());
            }

            // Add OwnedTrainingCourse requests
            List<OwnedTrainingCourse> ownedCourses = ownedTrainingCourseRepository
                    .findByStatusWithApprovedLecturer(PendingStatus.PENDING);

            System.out.println("Found " + ownedCourses.size() + " pending owned training course requests.");
            for (OwnedTrainingCourse course : ownedCourses) {
                requests.add(RequestFromLecturer.<OwnedTrainingCourseDTO>builder()
                        .content(ownedTrainingCourseMapper.toDTO(course))
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(course.getLecturer()))
                        .type(RequestLecturerType.OC)
                        .label(RequestLabel.Create)
                        .date(course.getUpdatedAt())
                        .build());
            }

            // Add ResearchProject requests
            List<ResearchProject> researchProjects = researchProjectRepository
                    .findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            System.out.println("Found " + researchProjects.size() + " pending research project requests.");
            for (ResearchProject project : researchProjects) {
                requests.add(RequestFromLecturer.<ResearchProjectDTO>builder()
                        .content(researchProjectMapper.toDTO(project))
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(project.getLecturer()))
                        .type(RequestLecturerType.RP)
                        .label(RequestLabel.Create)
                        .date(project.getUpdatedAt())
                        .build());
            }
            List<DegreeUpdateDTO> degreeUpdates = getPendingDegreeUpdates();
            for (DegreeUpdateDTO degreeUpdate : degreeUpdates) {
                requests.add(RequestFromLecturer.<DegreeUpdateDTO>builder()
                        .content(degreeUpdate)
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(
                                lecturerRepository.findByIdWithUser(degreeUpdate.getLecturer().getId())
                                        .orElseThrow(IllegalArgumentException::new)))
                        .type(RequestLecturerType.BC)
                        .label(RequestLabel.Update)
                        .date(degreeUpdate.getUpdate().getUpdatedAt())
                        .build());

            }
            List<CertificationUpdateDTO> certificationUpdates = getPendingCertificationUpdates();
            for (CertificationUpdateDTO certificationUpdate : certificationUpdates) {
                requests.add(RequestFromLecturer.<CertificationUpdateDTO>builder()
                        .content(certificationUpdate)
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(
                                lecturerRepository.findByIdWithUser(certificationUpdate.getLecturer().getId())
                                        .orElseThrow(IllegalArgumentException::new)))
                        .type(RequestLecturerType.CC)
                        .label(RequestLabel.Update)
                        .date(certificationUpdate.getUpdate().getUpdatedAt())
                        .build());
            }
            List<AttendedCourseUpdateDTO> attendedCourseUpdates = getPendingAttendedCourseUpdates();
            for (AttendedCourseUpdateDTO attendedCourseUpdate : attendedCourseUpdates) {
                requests.add(RequestFromLecturer.<AttendedCourseUpdateDTO>builder()
                        .content(attendedCourseUpdate)
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(
                                lecturerRepository.findByIdWithUser(attendedCourseUpdate.getLecturer().getId())
                                        .orElseThrow(IllegalArgumentException::new)))
                        .type(RequestLecturerType.AC)
                        .label(RequestLabel.Update)
                        .date(attendedCourseUpdate.getUpdate().getUpdatedAt())
                        .build());
            }
            List<OwnedCourseUpdateDTO> ownedCourseUpdates = getPendingOwnedCourseUpdates();
            for (OwnedCourseUpdateDTO ownedCourseUpdate : ownedCourseUpdates) {
                requests.add(RequestFromLecturer.<OwnedCourseUpdateDTO>builder()
                        .content(ownedCourseUpdate)
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(
                                lecturerRepository.findByIdWithUser(ownedCourseUpdate.getLecturer().getId())
                                        .orElseThrow(IllegalArgumentException::new)))
                        .type(RequestLecturerType.OC)
                        .label(RequestLabel.Update)
                        .date(ownedCourseUpdate.getUpdate().getUpdatedAt())
                        .build());
            }
            List<ResearchProjectUpdateDTO> researchProjectUpdates = getPendingResearchProjectUpdates();
            for (ResearchProjectUpdateDTO researchProjectUpdate : researchProjectUpdates) {
                requests.add(RequestFromLecturer.<ResearchProjectUpdateDTO>builder()
                        .content(researchProjectUpdate)
                        .lecturerInfo(Mapper.mapToLecturerInfoDTO(
                                lecturerRepository.findByIdWithUser(researchProjectUpdate.getLecturer().getId())
                                        .orElseThrow(IllegalArgumentException::new)))
                        .type(RequestLecturerType.RP)
                        .label(RequestLabel.Update)
                        .date(researchProjectUpdate.getUpdate().getUpdatedAt())
                        .build());
            }

            // Sort by date (newest first)
            requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));

            return requests;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching lecturer requests.", e);
        }
    }

    @Transactional
    public List<CourseInfoDTO> getAllCourses() {
        List<CourseInfoDTO> courseInfoDTOS = new ArrayList<>();
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            List<CourseMemberDTO> members = course.getCourseLecturers().stream()
                    .map(courseLecturer -> CourseMemberDTO.builder()
                            .lecturer(Mapper.mapToLecturerInfoDTO(courseLecturer.getLecturer()))
                            .courseRole(courseLecturer.getRole())
                            .build())
                    .toList();
            CourseInfoDTO courseInfo = CourseInfoDTO.builder()
                    .course(courseMapper.toDTO(course))
                    .members(members)
                    .build();
            courseInfoDTOS.add(courseInfo);
        }
        return courseInfoDTOS;
    }

    @Transactional
    public CourseDTO getCourseById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID không được để trống.");
        }
        try {
            UUID courseId;
            courseId = UUID.fromString(id);
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + id));
            return courseMapper.toDTO(course);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ID không hợp lệ.");
        }

    }

    @Transactional
    public CourseInfoDTO updateCourseMember(CourseInfoDTO req) {
        if (req == null || req.getCourse() == null || req.getCourse().getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Course course = courseRepository.findById(req.getCourse().getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học."));
        try {
            // Update course details
            // courseMapper. updateEntityFromDTO(req.getCourse(), course);
            // courseRepository.save(course);
            // courseRepository.flush();

            // Update course members
            List<CourseLecturer> existingMembers = course.getCourseLecturers();
            List<CourseLecturer> updatedMembers = req.getMembers().stream()
                    .map(member -> CourseLecturer.builder()
                            .course(course)
                            .lecturer(lecturerRepository.findById(member.getLecturer().getId())
                                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên.")))
                            .role(member.getCourseRole())
                            .build())
                    .toList();

            for (CourseLecturer existing : existingMembers) {
                for (CourseLecturer updated : updatedMembers) {
                    if (existing.getLecturer().getId().equals(updated.getLecturer().getId())) {
                        // Nếu role khác thì cập nhật
                        if (!existing.getRole().equals(updated.getRole())) {
                            existing.setRole(updated.getRole());
                        }
                    }
                }
            }

            // Remove old members not in the updated list
            existingMembers.removeIf(existingMember -> updatedMembers.stream().noneMatch(
                    updatedMember -> updatedMember.getLecturer().getId().equals(existingMember.getLecturer().getId())));

            // Add new members
            for (CourseLecturer updated : updatedMembers) {
                boolean alreadyExists = existingMembers.stream()
                        .anyMatch(existing -> existing.getLecturer().getId().equals(updated.getLecturer().getId()));
                if (!alreadyExists) {
                    existingMembers.add(updated);
                }
            }

            course.setCourseLecturers(existingMembers);
            courseRepository.save(course);
            return CourseInfoDTO.builder()
                    .course(courseMapper.toDTO(course))
                    .members(req.getMembers())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public List<OwnedCourseInfoDTO> getOwnedCourses() {
        try {
            List<OwnedTrainingCourse> ownedCourses = ownedTrainingCourseRepository.findAll();
            return ownedCourses.stream()
                    .filter(ownedTrainingCourse -> ownedTrainingCourse.getStatus() == PendingStatus.APPROVED
                            && ownedTrainingCourse.getLecturer() != null
                            && ownedTrainingCourse.getLecturer().getStatus() == PendingStatus.APPROVED
                            && ownedTrainingCourse.getCourse() == null)
                    .map(course -> OwnedCourseInfoDTO.builder()
                            .ownedCourse(ownedTrainingCourseMapper.toDTO(course))
                            .lecturer(Mapper.mapToLecturerInfoDTO(course.getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching owned courses.", e);
        }
    }

    @Transactional
    public CourseInfoDTO createCourse(CourseReq req) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        try {
            Course course = courseMapper.toEntity(req);
            if (req.getOwnedCourseId() != null) {
                Optional<OwnedTrainingCourse> ownedCourseOpt = ownedTrainingCourseRepository
                        .findById(req.getOwnedCourseId());
                if (ownedCourseOpt.isPresent()) {
                    OwnedTrainingCourse ownedCourse = ownedCourseOpt.get();
                    // Gán 2 chiều
                    course.setOwnedTrainingCourse(ownedCourse);
                    ownedCourse.setCourse(course);
                    ownedTrainingCourseRepository.save(ownedCourse);
                }
            }

            courseRepository.save(course);
            courseRepository.flush();

            CourseMemberDTO authorMember = null;
            if (req.getAuthorId() != null) {
                Lecturer lecturer = lecturerRepository.findById(req.getAuthorId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Không tìm thấy giảng viên với ID: " + req.getAuthorId()));

                CourseLecturer courseLecturer = CourseLecturer.builder()
                        .course(course)
                        .lecturer(lecturer)
                        .role(CourseRole.AUTHOR)
                        .build();

                courseLecturerRepository.save(courseLecturer);
                courseLecturerRepository.flush();

                authorMember = CourseMemberDTO.builder()
                        .lecturer(Mapper.mapToLecturerInfoDTO(lecturer))
                        .courseRole(CourseRole.AUTHOR)
                        .build();
            }

            return CourseInfoDTO.builder()
                    .course(courseMapper.toDTO(course))
                    .members(authorMember != null ? List.of(authorMember) : List.of())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo khóa học: " + e.getMessage(), e);
        }
    }

    @Transactional
    public CourseDTO updateCourse(CourseDTO req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("Dữ liệu yêu cầu không hợp lệ.");
        }
        Course course = courseRepository.findById(req.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + req.getId()));
        try {
            courseMapper.updateEntityFromDTO(req, course);
            courseRepository.save(course);
            courseRepository.flush();
            return courseMapper.toDTO(course);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật khóa học: " + e.getMessage(), e);
        }
    }
}
