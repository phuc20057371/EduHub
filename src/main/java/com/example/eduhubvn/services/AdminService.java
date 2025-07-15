package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.AllPendingEntityDTO;
import com.example.eduhubvn.dtos.AllPendingUpdateDTO;
import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.RejectReq;
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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private final LecturerMapper lecturerMapper;
    private final PartnerOrganizationMapper partnerOrganizationMapper;
    private final EducationInstitutionMapper educationInstitutionMapper;
    private final CertificationMapper certificationMapper;
    private final DegreeMapper degreeMapper;
    private final AttendedTrainingCourseMapper attendedTrainingCourseMapper;
    private final OwnedTrainingCourseMapper ownedTrainingCourseMapper;
    private final ResearchProjectMapper researchProjectMapper;


    /// Get

    @Transactional
    public AllPendingUpdateDTO getAllPendingUpdates() {
        try {
            List<LecturerUpdate> lecturerUpdates = lecturerUpdateRepository.findByStatus(PendingStatus.PENDING);
            List<PartnerOrganizationUpdate> partnerUpdates = partnerOrganizationUpdateRepository.findByStatus(PendingStatus.PENDING);
            List<EducationInstitutionUpdate> eduInsUpdates = educationInstitutionUpdateRepository.findByStatus(PendingStatus.PENDING);

            List<LecturerPendingDTO> lecturerDTOs = lecturerUpdates.stream()
                    .map(update -> new LecturerPendingDTO(
                            lecturerMapper.toDTO(update.getLecturer()),
                            lecturerMapper.toDTO(update)
                    )).toList();
            List<PartnerOrganizationPendingDTO> partnerDTOs = partnerUpdates.stream()
                    .map(update -> new PartnerOrganizationPendingDTO(
                            partnerOrganizationMapper.toDTO(update.getPartnerOrganization()),
                            partnerOrganizationMapper.toDTO(update)
                    )).toList();
            List<EducationInstitutionPendingDTO> eduDTOs = eduInsUpdates.stream()
                    .map(update -> new EducationInstitutionPendingDTO(
                            educationInstitutionMapper.toDTO(update.getEducationInstitution()),
                            educationInstitutionMapper.toDTO(update)
                    )).toList();
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
            List<PartnerOrganizationDTO> partners = partnerOrganizationRepository.findByStatus(PendingStatus.PENDING).stream()
                    .map(partnerOrganizationMapper::toDTO)
                    .toList();
            List<EducationInstitutionDTO> institutions = educationInstitutionRepository.findByStatus(PendingStatus.PENDING).stream()
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
        if (lecturer.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
        if (lecturer.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalArgumentException("Đã bị từ chối trước đó.");
        }
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
        if (update.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
        if (update.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
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
        if (educationInstitution.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
        if (institution.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
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
        if (update.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
        EducationInstitutionUpdate update = educationInstitutionUpdateRepository.findByIdAndStatus(req.getId(), PendingStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hồ sơ."));
        if (update.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
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
        if (partnerOrganization.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
        if (update.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
        if (update.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
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
        if (organization.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
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
        if (degree.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
        try {
            degree.setStatus(PendingStatus.APPROVED);
            Degree saved = degreeRepository.save(degree);
            degreeRepository.flush();
            return degreeMapper.toDTO(saved);
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
        if (update.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
            return degreeMapper.toDTO(degree);
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
        if (degree.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            degree.setStatus(PendingStatus.REJECTED);
            degree.setAdminNote(req.getAdminNote());
            degreeRepository.save(degree);
            degreeRepository.flush();
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
        if (update.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            degreeUpdateRepository.save(update);
            degreeUpdateRepository.flush();
            return degreeMapper.toDTO(update);
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
        if (certification.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
        try {
            certification.setStatus(PendingStatus.APPROVED);
            Certification saved = certificationRepository.save(certification);
            certificationRepository.flush();
            return certificationMapper.toDTO(saved);
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
        if (update.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
            return certificationMapper.toDTO(certification);
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
        if (certification.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            certification.setStatus(PendingStatus.REJECTED);
            certification.setAdminNote(req.getAdminNote());
            certificationRepository.save(certification);
            certificationRepository.flush();
            return certificationMapper.toDTO(certification);
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
        if (update.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            certificationUpdateRepository.saveAndFlush(update);
            return certificationMapper.toDTO(update);
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
        if (course.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
        try {
            course.setStatus(PendingStatus.APPROVED);
            course.setAdminNote("");
            attendedTrainingCourseRepository.saveAndFlush(course);
            return attendedTrainingCourseMapper.toDTO(course);
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
        if (update.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
            return attendedTrainingCourseMapper.toDTO(original);
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
        if (course.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            course.setStatus(PendingStatus.REJECTED);
            course.setAdminNote(req.getAdminNote());
            attendedTrainingCourseRepository.saveAndFlush(course);
            return attendedTrainingCourseMapper.toDTO(course);
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
        if (update.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            attendedTrainingCourseUpdateRepository.save(update);
            attendedTrainingCourseUpdateRepository.flush();
            return attendedTrainingCourseMapper.toDTO(update);
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
        if (course.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
        try {
            course.setStatus(PendingStatus.APPROVED);
            course.setAdminNote("");
            ownedTrainingCourseRepository.save(course);
            ownedTrainingCourseRepository.flush();
            return ownedTrainingCourseMapper.toDTO(course);
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
        if (update.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
            return ownedTrainingCourseMapper.toDTO(update);
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
        if (course.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            course.setStatus(PendingStatus.REJECTED);
            course.setAdminNote(req.getAdminNote());
            ownedTrainingCourseRepository.save(course);
            ownedTrainingCourseRepository.flush();
            return ownedTrainingCourseMapper.toDTO(course);
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
        if (update.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            ownedTrainingCourseUpdateRepository.save(update);
            ownedTrainingCourseUpdateRepository.flush();
            return ownedTrainingCourseMapper.toDTO(update);
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
        if (project.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
        try {
            project.setStatus(PendingStatus.APPROVED);
            project.setAdminNote("");
            researchProjectRepository.save(project);
            researchProjectRepository.flush();
            return researchProjectMapper.toDTO(project);
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
        if (update.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Đã được phê duyệt trước đó.");
        }
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
            return researchProjectMapper.toDTO(original);
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
        if (project.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó");
        }
        try {
            project.setStatus(PendingStatus.REJECTED);
            project.setAdminNote(req.getAdminNote());
            researchProjectRepository.save(project);
            researchProjectRepository.flush();
            return researchProjectMapper.toDTO(project);
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
        if (project.getStatus() == PendingStatus.REJECTED) {
            throw new IllegalStateException("Đã bị từ chối trước đó.");
        }
        try {
            project.setStatus(PendingStatus.REJECTED);
            project.setAdminNote(req.getAdminNote());
            researchProjectUpdateRepository.save(project);
            researchProjectUpdateRepository.flush();
            return researchProjectMapper.toDTO(project);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public List<DegreePendingDTO> getPendingDegreeUpdates() {
        try {
            List<DegreeUpdate> pendingUpdates = degreeUpdateRepository.findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getDegree() != null
                            && update.getDegree().getStatus() == PendingStatus.APPROVED)
                    .map(update -> DegreePendingDTO.builder()
                            .degree(degreeMapper.toDTO(update.getDegree()))
                            .updatedDegree(degreeMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getDegree().getLecturer()))
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
}
