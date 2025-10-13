package com.example.eduhubvn.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.eduhubvn.dtos.AllPendingEntityDTO;
import com.example.eduhubvn.dtos.AllPendingUpdateDTO;
import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.MessageSocket;
import com.example.eduhubvn.dtos.MessageSocketType;
import com.example.eduhubvn.dtos.PaginatedResponse;
import com.example.eduhubvn.dtos.RejectReq;
import com.example.eduhubvn.dtos.RequestFromLecturer;
import com.example.eduhubvn.dtos.RequestLabel;
import com.example.eduhubvn.dtos.RequestLecturerType;
import com.example.eduhubvn.dtos.admin.request.RegisterInstitutionFromAdminRequest;
import com.example.eduhubvn.dtos.admin.request.RegisterLecturerFromAdminRequest;
import com.example.eduhubvn.dtos.admin.request.RegisterPartnerFromAdminRequest;
import com.example.eduhubvn.dtos.course.CourseDTO;
import com.example.eduhubvn.dtos.course.CourseInfoDTO;
import com.example.eduhubvn.dtos.course.CourseMemberDTO;
import com.example.eduhubvn.dtos.course.CourseReq;
import com.example.eduhubvn.dtos.course.OwnedCourseInfoDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.AttendedCourseUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.AttendedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.CertificationDTO;
import com.example.eduhubvn.dtos.lecturer.CertificationUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeDTO;
import com.example.eduhubvn.dtos.lecturer.DegreePendingCreateDTO;
import com.example.eduhubvn.dtos.lecturer.DegreeUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerAllInfoDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerAllProfileDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerPendingDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.OwnedCourseUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.OwnedTrainingCourseDTO;
import com.example.eduhubvn.dtos.lecturer.ResearchProjectDTO;
import com.example.eduhubvn.dtos.lecturer.ResearchProjectUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.request.AttendedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.CertificationReq;
import com.example.eduhubvn.dtos.lecturer.request.DegreeReq;
import com.example.eduhubvn.dtos.lecturer.request.OwnedTrainingCourseReq;
import com.example.eduhubvn.dtos.lecturer.request.ResearchProjectReq;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.AttendedTrainingCourseUpdate;
import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.CertificationUpdate;
import com.example.eduhubvn.entities.Course;
import com.example.eduhubvn.entities.CourseLecturer;
import com.example.eduhubvn.entities.CourseRole;
import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.DegreeUpdate;
import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.EducationInstitutionUpdate;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.LecturerUpdate;
import com.example.eduhubvn.entities.OwnedTrainingCourse;
import com.example.eduhubvn.entities.OwnedTrainingCourseUpdate;
import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PartnerOrganizationUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.ResearchProject;
import com.example.eduhubvn.entities.ResearchProjectUpdate;
import com.example.eduhubvn.entities.Role;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.AttendedTrainingCourseMapper;
import com.example.eduhubvn.mapper.CertificationMapper;
import com.example.eduhubvn.mapper.CourseMapper;
import com.example.eduhubvn.mapper.DegreeMapper;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.mapper.OwnedTrainingCourseMapper;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.mapper.ResearchProjectMapper;
import com.example.eduhubvn.repositories.AttendedTrainingCourseRepository;
import com.example.eduhubvn.repositories.AttendedTrainingCourseUpdateRepository;
import com.example.eduhubvn.repositories.CertificationRepository;
import com.example.eduhubvn.repositories.CertificationUpdateRepository;
import com.example.eduhubvn.repositories.CourseLecturerRepository;
import com.example.eduhubvn.repositories.CourseRepository;
import com.example.eduhubvn.repositories.DegreeRepository;
import com.example.eduhubvn.repositories.DegreeUpdateRepository;
import com.example.eduhubvn.repositories.EducationInstitutionRepository;
import com.example.eduhubvn.repositories.EducationInstitutionUpdateRepository;
import com.example.eduhubvn.repositories.LecturerRepository;
import com.example.eduhubvn.repositories.LecturerUpdateRepository;
import com.example.eduhubvn.repositories.OwnedTrainingCourseRepository;
import com.example.eduhubvn.repositories.OwnedTrainingCourseUpdateRepository;
import com.example.eduhubvn.repositories.PartnerOrganizationRepository;
import com.example.eduhubvn.repositories.PartnerOrganizationUpdateRepository;
import com.example.eduhubvn.repositories.ResearchProjectRepository;
import com.example.eduhubvn.repositories.ResearchProjectUpdateRepository;
import com.example.eduhubvn.repositories.UserRepository;
import com.example.eduhubvn.ulti.Mapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
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
    private final PasswordEncoder passwordEncoder;

    /// General

    @Transactional
    public AllPendingUpdateDTO getAllPendingUpdates() {
        try {
            List<LecturerUpdate> lecturerUpdates = lecturerUpdateRepository.findByStatus(PendingStatus.PENDING);
            List<PartnerOrganizationUpdate> partnerUpdates = partnerOrganizationUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            List<EducationInstitutionUpdate> eduInsUpdates = educationInstitutionUpdateRepository
                    .findByStatus(PendingStatus.PENDING);

            List<LecturerPendingDTO> lecturerDTOs = lecturerUpdates.stream()
                    .filter(update -> update.getLecturer() != null && !update.getLecturer().isHidden())
                    .map(update -> new LecturerPendingDTO(
                            lecturerMapper.toDTO(update.getLecturer()),
                            lecturerMapper.toDTO(update)))
                    .toList();
            List<PartnerOrganizationPendingDTO> partnerDTOs = partnerUpdates.stream()
                    .filter(update -> update.getPartnerOrganization() != null
                            && !update.getPartnerOrganization().isHidden())
                    .map(update -> new PartnerOrganizationPendingDTO(
                            partnerOrganizationMapper.toDTO(update.getPartnerOrganization()),
                            partnerOrganizationMapper.toDTO(update)))
                    .toList();
            List<EducationInstitutionPendingDTO> eduDTOs = eduInsUpdates.stream()
                    .filter(update -> update.getEducationInstitution() != null
                            && !update.getEducationInstitution().isHidden())
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
        try {
            lecturer.getUser().setRole(Role.LECTURER);
            lecturer.setStatus(PendingStatus.APPROVED);
            lecturer.setAdminNote("");
            if (lecturer.getLecturerId() == null || lecturer.getLecturerId().isBlank()) {
                lecturer.setLecturerId(generateNextLecturerId());
            }
            lecturerRepository.save(lecturer);
            lecturerRepository.flush();
            LecturerDTO dto = lecturerMapper.toDTO(lecturer);
            messagingTemplate.convertAndSend("/topic/USER/" + lecturer.getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_LECTURER, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextLecturerId() {
        Optional<Lecturer> lastLecturerOpt = lecturerRepository.findTopByOrderByLecturerIdDesc();
        int nextNumber = 1;
        if (lastLecturerOpt.isPresent()) {
            String lastId = lastLecturerOpt.get().getLecturerId(); // VD: GV023
            String numberPart = lastId.substring(2); // "023"
            nextNumber = Integer.parseInt(numberPart) + 1;
        }
        return String.format("GV%03d", nextNumber); // => GV001, GV002, GV123
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
            LecturerDTO dto = lecturerMapper.toDTO(lecturer);
            messagingTemplate.convertAndSend("/topic/USER/" + lecturer.getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_LECTURER, dto));
            return dto;
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
            LecturerDTO dto = lecturerMapper.toDTO(lecturer);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + lecturer.getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_LECTURER_UPDATE, dto));
            return dto;
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

            LecturerUpdateDTO dto = lecturerMapper.toDTO(update);
            messagingTemplate.convertAndSend("/topic/LECTURER/" + update.getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_LECTURER_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LecturerAllProfileDTO getLecturerAllProfile(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên."));
        LecturerUpdate lecturerUpdate = lecturerUpdateRepository
                .findByLecturerAndStatus(lecturer, PendingStatus.PENDING)
                .orElse(null);
        List<Degree> degrees = degreeRepository.findByLecturer(lecturer);
        List<Certification> certifications = certificationRepository.findByLecturer(lecturer);
        List<OwnedTrainingCourse> ownedCourses = ownedTrainingCourseRepository.findByLecturer(lecturer);
        List<AttendedTrainingCourse> attendedCourses = attendedTrainingCourseRepository.findByLecturer(lecturer);
        List<ResearchProject> researchProjects = researchProjectRepository.findByLecturer(lecturer);

        List<DegreeUpdateDTO> degreeUpdates = new ArrayList<>();
        List<CertificationUpdateDTO> certificationUpdates = new ArrayList<>();
        List<OwnedCourseUpdateDTO> ownedCourseUpdates = new ArrayList<>();
        List<AttendedCourseUpdateDTO> attendedCourseUpdates = new ArrayList<>();
        List<ResearchProjectUpdateDTO> researchProjectUpdates = new ArrayList<>();

        for (Degree degree : degrees) {
            DegreeUpdateDTO deg = DegreeUpdateDTO.builder()
                    .lecturer(lecturerMapper.toDTO(degree.getLecturer()))
                    .original(degreeMapper.toDTO(degree))
                    .update(degreeMapper.toDTO(degree.getDegreeUpdate()))
                    .build();
            degreeUpdates.add(deg);
        }
        for (Certification certification : certifications) {
            CertificationUpdateDTO cert = CertificationUpdateDTO.builder()
                    .lecturer(lecturerMapper.toDTO(certification.getLecturer()))
                    .original(certificationMapper.toDTO(certification))
                    .update(certificationMapper.toDTO(certification.getCertificationUpdate()))
                    .build();
            certificationUpdates.add(cert);
        }
        for (OwnedTrainingCourse ownedCourse : ownedCourses) {
            OwnedCourseUpdateDTO ownedCourseUpdate = OwnedCourseUpdateDTO.builder()
                    .lecturer(lecturerMapper.toDTO(ownedCourse.getLecturer()))
                    .original(ownedTrainingCourseMapper.toDTO(ownedCourse))
                    .update(ownedTrainingCourseMapper.toDTO(ownedCourse.getOwnedTrainingCourseUpdate()))
                    .build();
            ownedCourseUpdates.add(ownedCourseUpdate);
        }
        for (AttendedTrainingCourse attendedCourse : attendedCourses) {
            AttendedCourseUpdateDTO attendedCourseUpdate = AttendedCourseUpdateDTO.builder()
                    .lecturer(lecturerMapper.toDTO(attendedCourse.getLecturer()))
                    .original(attendedTrainingCourseMapper.toDTO(attendedCourse))
                    .update(attendedTrainingCourseMapper.toDTO(attendedCourse.getAttendedTrainingCourseUpdate()))
                    .build();
            attendedCourseUpdates.add(attendedCourseUpdate);
        }
        for (ResearchProject researchProject : researchProjects) {
            ResearchProjectUpdateDTO researchProjectUpdate = ResearchProjectUpdateDTO.builder()
                    .lecturer(lecturerMapper.toDTO(researchProject.getLecturer()))
                    .original(researchProjectMapper.toDTO(researchProject))
                    .update(researchProjectMapper.toDTO(researchProject.getResearchProjectUpdate()))
                    .build();
            researchProjectUpdates.add(researchProjectUpdate);
        }

        return LecturerAllProfileDTO.builder()
                .lecturer(Mapper.mapToLecturerInfoDTO(lecturer))
                .lecturerUpdate(lecturerMapper.toDTO(lecturerUpdate))
                .degrees(degreeUpdates)
                .certifications(certificationUpdates)
                .ownedCourses(ownedCourseUpdates)
                .attendedCourses(attendedCourseUpdates)
                .researchProjects(researchProjectUpdates)
                .build();
    }

    public LecturerDTO createLecturer(RegisterLecturerFromAdminRequest req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("email");
        }
        if (lecturerRepository.findByCitizenId(req.getCitizenId()).isPresent()) {
            throw new IllegalArgumentException("citizenId");
        }
        User newUser = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword())) // Mật khẩu nên được mã hóa trước khi lưu
                .role(Role.LECTURER)
                .build();
        userRepository.save(newUser);
        Lecturer lecturer = Lecturer.builder()
                .citizenId(req.getCitizenId())
                .phoneNumber(req.getPhoneNumber())
                .fullName(req.getFullName())
                .dateOfBirth(req.getDateOfBirth())
                .gender(req.getGender())
                .bio(req.getBio())
                .address(req.getAddress())
                .avatarUrl(req.getAvatarUrl())
                .academicRank(req.getAcademicRank())
                .specialization(req.getSpecialization())
                .experienceYears(req.getExperienceYears())
                .jobField(req.getJobField())
                .status(PendingStatus.APPROVED)
                .user(newUser) // gắn user
                .build();
        newUser.setLecturer(lecturer);
        userRepository.save(newUser);
        return lecturerMapper.toDTO(lecturer);

    }

    @Transactional
    public void deleteLecturer(IdRequest id) {
        if (id == null || id.getId() == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(id.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên."));
        try {
            User user = lecturer.getUser();

            // Clean up pending update records for this lecturer
            cleanupLecturerPendingUpdates(lecturer);

            // Đánh dấu lecturer là ẩn và vô hiệu hóa user thay vì xóa
            lecturer.setHidden(true);
            user.setEnabled(false);

            lecturerRepository.save(lecturer);
            userRepository.save(user);
            lecturerRepository.flush();
            userRepository.flush();

            messagingTemplate.convertAndSend("/topic/LECTURER/" + user.getId(),
                    new MessageSocket(MessageSocketType.DELETE_LECTURER, null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanupLecturerPendingUpdates(Lecturer lecturer) {
        try {
            // Clean up degree updates
            List<DegreeUpdate> degreeUpdates = degreeUpdateRepository.findByStatus(PendingStatus.PENDING).stream()
                    .filter(update -> update.getDegree() != null
                            && update.getDegree().getLecturer() != null
                            && update.getDegree().getLecturer().getId().equals(lecturer.getId()))
                    .toList();
            for (DegreeUpdate update : degreeUpdates) {
                update.setStatus(PendingStatus.REJECTED);
                update.setAdminNote("Giảng viên đã bị xóa khỏi hệ thống");
            }
            degreeUpdateRepository.saveAll(degreeUpdates);

            // Clean up certification updates
            List<CertificationUpdate> certificationUpdates = certificationUpdateRepository
                    .findByStatus(PendingStatus.PENDING).stream()
                    .filter(update -> update.getCertification() != null
                            && update.getCertification().getLecturer() != null
                            && update.getCertification().getLecturer().getId().equals(lecturer.getId()))
                    .toList();
            for (CertificationUpdate update : certificationUpdates) {
                update.setStatus(PendingStatus.REJECTED);
                update.setAdminNote("Giảng viên đã bị xóa khỏi hệ thống");
            }
            certificationUpdateRepository.saveAll(certificationUpdates);

            // Clean up attended course updates
            List<AttendedTrainingCourseUpdate> attendedCourseUpdates = attendedTrainingCourseUpdateRepository
                    .findByStatus(PendingStatus.PENDING).stream()
                    .filter(update -> update.getAttendedTrainingCourse() != null
                            && update.getAttendedTrainingCourse().getLecturer() != null
                            && update.getAttendedTrainingCourse().getLecturer().getId().equals(lecturer.getId()))
                    .toList();
            for (AttendedTrainingCourseUpdate update : attendedCourseUpdates) {
                update.setStatus(PendingStatus.REJECTED);
                update.setAdminNote("Giảng viên đã bị xóa khỏi hệ thống");
            }
            attendedTrainingCourseUpdateRepository.saveAll(attendedCourseUpdates);

            // Clean up owned course updates
            List<OwnedTrainingCourseUpdate> ownedCourseUpdates = ownedTrainingCourseUpdateRepository
                    .findByStatus(PendingStatus.PENDING).stream()
                    .filter(update -> update.getOwnedTrainingCourse() != null
                            && update.getOwnedTrainingCourse().getLecturer() != null
                            && update.getOwnedTrainingCourse().getLecturer().getId().equals(lecturer.getId()))
                    .toList();
            for (OwnedTrainingCourseUpdate update : ownedCourseUpdates) {
                update.setStatus(PendingStatus.REJECTED);
                update.setAdminNote("Giảng viên đã bị xóa khỏi hệ thống");
            }
            ownedTrainingCourseUpdateRepository.saveAll(ownedCourseUpdates);

            // Clean up research project updates
            List<ResearchProjectUpdate> researchProjectUpdates = researchProjectUpdateRepository
                    .findByStatus(PendingStatus.PENDING).stream()
                    .filter(update -> update.getResearchProject() != null
                            && update.getResearchProject().getLecturer() != null
                            && update.getResearchProject().getLecturer().getId().equals(lecturer.getId()))
                    .toList();
            for (ResearchProjectUpdate update : researchProjectUpdates) {
                update.setStatus(PendingStatus.REJECTED);
                update.setAdminNote("Giảng viên đã bị xóa khỏi hệ thống");
            }
            researchProjectUpdateRepository.saveAll(researchProjectUpdates);

        } catch (Exception e) {
            // Log the error but don't fail the lecturer deletion
            System.err.println(
                    "Error cleaning up pending updates for lecturer " + lecturer.getId() + ": " + e.getMessage());
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
            EducationInstitutionDTO dto = educationInstitutionMapper.toDTO(educationInstitution);
            messagingTemplate.convertAndSend("/topic/USER/" + educationInstitution.getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_INSTITUTION, dto));
            return dto;
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
            EducationInstitutionDTO dto = educationInstitutionMapper.toDTO(institution);
            messagingTemplate.convertAndSend("/topic/USER/" + institution.getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_INSTITUTION, dto));
            return dto;
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
            EducationInstitutionDTO dto = educationInstitutionMapper.toDTO(educationInstitution);
            messagingTemplate.convertAndSend("/topic/SCHOOL/" + educationInstitution.getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_INSTITUTION_UPDATE, dto));
            return dto;
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
            EducationInstitutionUpdateDTO dto = educationInstitutionMapper.toDTO(update);
            messagingTemplate.convertAndSend("/topic/SCHOOL/" + update.getEducationInstitution().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_INSTITUTION_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteInstitution(IdRequest id) {
        if (id == null || id.getId() == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        EducationInstitution institution = educationInstitutionRepository.findById(id.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tổ chức giáo dục."));
        try {
            User user = institution.getUser();

            // Clean up pending update records for this institution
            cleanupInstitutionPendingUpdates(institution);

            // Đánh dấu institution là ẩn và vô hiệu hóa user thay vì xóa
            institution.setHidden(true);
            user.setEnabled(false);
            educationInstitutionRepository.save(institution);
            userRepository.save(user);
            messagingTemplate.convertAndSend("/topic/SCHOOL/" + user.getId(),
                    new MessageSocket(MessageSocketType.DELETE_INSTITUTION, null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanupInstitutionPendingUpdates(EducationInstitution institution) {
        try {
            // Clean up education institution updates
            List<EducationInstitutionUpdate> institutionUpdates = educationInstitutionUpdateRepository
                    .findByStatus(PendingStatus.PENDING).stream()
                    .filter(update -> update.getEducationInstitution() != null
                            && update.getEducationInstitution().getId().equals(institution.getId()))
                    .toList();
            for (EducationInstitutionUpdate update : institutionUpdates) {
                update.setStatus(PendingStatus.REJECTED);
                update.setAdminNote("Tổ chức giáo dục đã bị xóa khỏi hệ thống");
            }
            educationInstitutionUpdateRepository.saveAll(institutionUpdates);

        } catch (Exception e) {
            // Log the error but don't fail the institution deletion
            System.err.println(
                    "Error cleaning up pending updates for institution " + institution.getId() + ": " + e.getMessage());
        }
    }

    public EducationInstitutionDTO createInstitution(RegisterInstitutionFromAdminRequest req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("email");
        }
        if (educationInstitutionRepository.existsByBusinessRegistrationNumber(req.getBusinessRegistrationNumber())) {
            throw new IllegalArgumentException("businessId");
        }
        User newUser = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(passwordEncoder.encode(req.getPassword()))) // Mật khẩu nên được mã hóa
                                                                                             // trước khi lưu
                .role(Role.SCHOOL)
                .build();
        userRepository.save(newUser);
        EducationInstitution institution = EducationInstitution.builder()
                .businessRegistrationNumber(req.getBusinessRegistrationNumber())
                .institutionName(req.getInstitutionName())
                .institutionType(req.getInstitutionType())
                .phoneNumber(req.getPhoneNumber())
                .website(req.getWebsite())
                .address(req.getAddress())
                .representativeName(req.getRepresentativeName())
                .position(req.getPosition())
                .description(req.getDescription())
                .logoUrl(req.getLogoUrl())
                .establishedYear(req.getEstablishedYear())
                .status(PendingStatus.APPROVED)
                .user(newUser) // gắn user
                .build();
        newUser.setEducationInstitution(institution);
        userRepository.save(newUser);
        return educationInstitutionMapper.toDTO(institution);
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
            PartnerOrganizationDTO dto = partnerOrganizationMapper.toDTO(partnerOrganization);
            messagingTemplate.convertAndSend("/topic/USER/" + partnerOrganization.getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_PARTNER, dto));
            return dto;
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
            PartnerOrganizationDTO dto = partnerOrganizationMapper.toDTO(partnerOrganization);
            messagingTemplate.convertAndSend("/topic/ORGANIZATION/" + partnerOrganization.getUser().getId(),
                    new MessageSocket(MessageSocketType.APPROVE_PARTNER_UPDATE, dto));
            return dto;
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
            PartnerOrganizationUpdateDTO dto = partnerOrganizationMapper.toDTO(update);
            messagingTemplate.convertAndSend("/topic/ORGANIZATION/" + update.getPartnerOrganization().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_PARTNER_UPDATE, dto));
            return dto;
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
            PartnerOrganizationDTO dto = partnerOrganizationMapper.toDTO(organization);
            messagingTemplate.convertAndSend("/topic/USER/" + organization.getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_PARTNER, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deletePartner(IdRequest id) {
        if (id == null || id.getId() == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        PartnerOrganization organization = partnerOrganizationRepository.findById(id.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tổ chức đối tác."));
        try {
            User user = organization.getUser();

            // Clean up pending update records for this partner organization
            cleanupPartnerPendingUpdates(organization);

            // Đánh dấu organization là ẩn và vô hiệu hóa user thay vì xóa
            organization.setHidden(true);
            user.setEnabled(false);
            partnerOrganizationRepository.save(organization);
            userRepository.save(user);
            messagingTemplate.convertAndSend("/topic/ORGANIZATION/" + user.getId(),
                    new MessageSocket(MessageSocketType.DELETE_PARTNER, null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanupPartnerPendingUpdates(PartnerOrganization organization) {
        try {
            // Clean up partner organization updates
            List<PartnerOrganizationUpdate> partnerUpdates = partnerOrganizationUpdateRepository
                    .findByStatus(PendingStatus.PENDING).stream()
                    .filter(update -> update.getPartnerOrganization() != null
                            && update.getPartnerOrganization().getId().equals(organization.getId()))
                    .toList();
            for (PartnerOrganizationUpdate update : partnerUpdates) {
                update.setStatus(PendingStatus.REJECTED);
                update.setAdminNote("Tổ chức đối tác đã bị xóa khỏi hệ thống");
            }
            partnerOrganizationUpdateRepository.saveAll(partnerUpdates);

        } catch (Exception e) {
            // Log the error but don't fail the partner deletion
            System.err.println(
                    "Error cleaning up pending updates for partner " + organization.getId() + ": " + e.getMessage());
        }
    }

    public PartnerOrganizationDTO createPartner(RegisterPartnerFromAdminRequest req, User user) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("email");
        }
        if (partnerOrganizationRepository.existsByBusinessRegistrationNumber(req.getBusinessRegistrationNumber())) {
            throw new IllegalArgumentException("businessId");
        }
        User newUser = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword())) // Mật khẩu nên được mã hóa trước khi lưu
                .role(Role.ORGANIZATION)
                .build();
        userRepository.save(newUser);
        PartnerOrganization organization = PartnerOrganization.builder()
                .businessRegistrationNumber(req.getBusinessRegistrationNumber())
                .organizationName(req.getOrganizationName())
                .industry(req.getIndustry())
                .phoneNumber(req.getPhoneNumber())
                .website(req.getWebsite())
                .address(req.getAddress())
                .representativeName(req.getRepresentativeName())
                .position(req.getPosition())
                .description(req.getDescription())
                .logoUrl(req.getLogoUrl())
                .establishedYear(req.getEstablishedYear())
                .status(PendingStatus.APPROVED)
                .user(newUser) // gắn user
                .build();
        newUser.setPartnerOrganization(organization);
        userRepository.save(newUser);
        return partnerOrganizationMapper.toDTO(organization);
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

    @Transactional
    public List<DegreeUpdateDTO> getPendingDegreeUpdates() {
        try {
            List<DegreeUpdate> pendingUpdates = degreeUpdateRepository.findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getDegree() != null
                            && update.getDegree().getStatus() == PendingStatus.APPROVED
                            && update.getDegree().getLecturer() != null
                            && !update.getDegree().getLecturer().isHidden())
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

    public DegreeDTO createDegree(DegreeReq req, UUID lecturerId) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên."));
        Degree degree = degreeMapper.toEntity(req);
        degree.setLecturer(lecturer);
        degree.setStatus(PendingStatus.APPROVED);
        degreeRepository.save(degree);
        return degreeMapper.toDTO(degree);
    }

    public void deleteDegree(IdRequest id) {
        if (id == null || id.getId() == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        Degree degree = degreeRepository.findById(id.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bằng cấp."));
        try {
            User user = degree.getLecturer().getUser();
            degreeRepository.delete(degree);
            degreeRepository.flush();
            messagingTemplate.convertAndSend("/topic/LECTURER/" + user.getId(),
                    new MessageSocket(MessageSocketType.DELETE_DEGREE, null));
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
            messagingTemplate.convertAndSend(
                    "/topic/LECTURER/" + update.getCertification().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_CERTIFICATION_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CertificationDTO createCertification(CertificationReq req, UUID lecturerId) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên."));
        Certification certification = certificationMapper.toEntity(req);
        certification.setLecturer(lecturer);
        certification.setStatus(PendingStatus.APPROVED);
        certificationRepository.save(certification);
        return certificationMapper.toDTO(certification);
    };

    public void deleteCertification(IdRequest id) {
        if (id == null || id.getId() == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        Certification certification = certificationRepository.findById(id.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chứng chỉ."));
        try {
            User user = certification.getLecturer().getUser();
            certificationRepository.delete(certification);
            certificationRepository.flush();
            messagingTemplate.convertAndSend("/topic/LECTURER/" + user.getId(),
                    new MessageSocket(MessageSocketType.DELETE_CERTIFICATION, null));
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
            messagingTemplate.convertAndSend(
                    "/topic/LECTURER/" + update.getAttendedTrainingCourse().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_ATTENDED_COURSE_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AttendedTrainingCourseDTO createAttendedCourse(AttendedTrainingCourseReq req, UUID lecturerId) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên."));
        AttendedTrainingCourse course = attendedTrainingCourseMapper.toEntity(req);
        course.setLecturer(lecturer);
        course.setStatus(PendingStatus.APPROVED);
        attendedTrainingCourseRepository.save(course);
        return attendedTrainingCourseMapper.toDTO(course);
    }

    public void deleteAttendedCourse(IdRequest id) {
        if (id == null || id.getId() == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        AttendedTrainingCourse course = attendedTrainingCourseRepository.findById(id.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khoá học đã tham gia."));
        try {
            User user = course.getLecturer().getUser();
            attendedTrainingCourseRepository.delete(course);
            attendedTrainingCourseRepository.flush();
            messagingTemplate.convertAndSend("/topic/LECTURER/" + user.getId(),
                    new MessageSocket(MessageSocketType.DELETE_ATTENDED_COURSE, null));
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
            messagingTemplate.convertAndSend(
                    "/topic/LECTURER/" + update.getOwnedTrainingCourse().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_OWNED_COURSE_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OwnedTrainingCourseDTO createOwnedCourse(OwnedTrainingCourseReq req, UUID lecturerId) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên."));
        OwnedTrainingCourse course = ownedTrainingCourseMapper.toEntity(req);
        course.setLecturer(lecturer);
        course.setStatus(PendingStatus.APPROVED);
        ownedTrainingCourseRepository.save(course);
        return ownedTrainingCourseMapper.toDTO(course);
    }

    public void deleteOwnedCourse(IdRequest id) {
        if (id == null || id.getId() == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        OwnedTrainingCourse course = ownedTrainingCourseRepository.findById(id.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khoá học đã sở hữu."));
        try {
            User user = course.getLecturer().getUser();
            ownedTrainingCourseRepository.delete(course);
            ownedTrainingCourseRepository.flush();
            messagingTemplate.convertAndSend("/topic/LECTURER/" + user.getId(),
                    new MessageSocket(MessageSocketType.DELETE_OWNED_COURSE, null));
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
            messagingTemplate.convertAndSend(
                    "/topic/LECTURER/" + project.getResearchProject().getLecturer().getUser().getId(),
                    new MessageSocket(MessageSocketType.REJECT_RESEARCH_PROJECT_UPDATE, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResearchProjectDTO createResearchProject(ResearchProjectReq req, UUID lecturerId) {
        if (req == null) {
            throw new IllegalArgumentException("Dữ liệu không được trống.");
        }
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy giảng viên."));
        ResearchProject project = researchProjectMapper.toEntity(req);
        project.setLecturer(lecturer);
        project.setStatus(PendingStatus.APPROVED);
        researchProjectRepository.save(project);
        return researchProjectMapper.toDTO(project);
    }

    public void deleteResearchProject(IdRequest id) {
        if (id == null || id.getId() == null) {
            throw new IllegalArgumentException("ID không được trống.");
        }
        ResearchProject project = researchProjectRepository.findById(id.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đề tài nghiên cứu."));
        try {
            User user = project.getLecturer().getUser();
            researchProjectRepository.delete(project);
            researchProjectRepository.flush();
            messagingTemplate.convertAndSend("/topic/LECTURER/" + user.getId(),
                    new MessageSocket(MessageSocketType.DELETE_RESEARCH_PROJECT, null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //////////////////////
    /// /////////////////////
    /// //////////////////////

    @Transactional
    public List<CertificationUpdateDTO> getPendingCertificationUpdates() {
        try {
            List<CertificationUpdate> pendingUpdates = certificationUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getCertification() != null
                            && update.getCertification().getStatus() == PendingStatus.APPROVED
                            && update.getCertification().getLecturer() != null
                            && !update.getCertification().getLecturer().isHidden())
                    .map(update -> CertificationUpdateDTO.builder()
                            .original(certificationMapper.toDTO(update.getCertification()))
                            .update(certificationMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getCertification().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending certification updates.", e);
        }
    }

    @Transactional
    public List<AttendedCourseUpdateDTO> getPendingAttendedCourseUpdates() {
        try {
            List<AttendedTrainingCourseUpdate> pendingUpdates = attendedTrainingCourseUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getAttendedTrainingCourse() != null
                            && update.getAttendedTrainingCourse().getStatus() == PendingStatus.APPROVED
                            && update.getAttendedTrainingCourse().getLecturer() != null
                            && !update.getAttendedTrainingCourse().getLecturer().isHidden())
                    .map(update -> AttendedCourseUpdateDTO.builder()
                            .original(attendedTrainingCourseMapper.toDTO(update.getAttendedTrainingCourse()))
                            .update(attendedTrainingCourseMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getAttendedTrainingCourse().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending attended course updates.", e);
        }
    }

    @Transactional
    public List<OwnedCourseUpdateDTO> getPendingOwnedCourseUpdates() {
        try {
            List<OwnedTrainingCourseUpdate> pendingUpdates = ownedTrainingCourseUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getOwnedTrainingCourse() != null
                            && update.getOwnedTrainingCourse().getStatus() == PendingStatus.APPROVED
                            && update.getOwnedTrainingCourse().getLecturer() != null
                            && !update.getOwnedTrainingCourse().getLecturer().isHidden())
                    .map(update -> OwnedCourseUpdateDTO.builder()
                            .original(ownedTrainingCourseMapper.toDTO(update.getOwnedTrainingCourse()))
                            .update(ownedTrainingCourseMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getOwnedTrainingCourse().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending owned course updates.", e);
        }
    }

    @Transactional
    public List<ResearchProjectUpdateDTO> getPendingResearchProjectUpdates() {
        try {
            List<ResearchProjectUpdate> pendingUpdates = researchProjectUpdateRepository
                    .findByStatus(PendingStatus.PENDING);
            return pendingUpdates.stream()
                    .filter(update -> update.getResearchProject() != null
                            && update.getResearchProject().getStatus() == PendingStatus.APPROVED
                            && update.getResearchProject().getLecturer() != null
                            && !update.getResearchProject().getLecturer().isHidden())
                    .map(update -> ResearchProjectUpdateDTO.builder()
                            .original(researchProjectMapper.toDTO(update.getResearchProject()))
                            .update(researchProjectMapper.toDTO(update))
                            .lecturer(lecturerMapper.toDTO(update.getResearchProject().getLecturer()))
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending research project updates.", e);
        }
    }

    @Transactional
    public List<DegreePendingCreateDTO> getPendingDegreeCreate() {
        try {
            List<Degree> pending = degreeRepository.findByStatus(PendingStatus.PENDING);
            return pending.stream()
                    .filter(d -> d.getLecturer() != null
                            && d.getLecturer().getStatus() == PendingStatus.APPROVED
                            && !d.getLecturer().isHidden())
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

    @Transactional
    public PaginatedResponse<LecturerInfoDTO> getAllLecturersPaginated(int page, int size) {
        try {
            // Validate pagination parameters
            if (page < 0) {
                page = 0;
            }
            if (size <= 0) {
                size = 10; // Default page size
            }
            if (size > 100) {
                size = 100; // Maximum page size
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<Lecturer> lecturerPage = lecturerRepository.findAll(pageable);

            List<LecturerInfoDTO> lecturerDTOs = lecturerPage.getContent().stream()
                    .map(this::mapToLecturerInfoDTO)
                    .toList();

            return PaginatedResponse.of(
                    lecturerDTOs,
                    page,
                    size,
                    lecturerPage.getTotalElements());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching paginated lecturers.", e);
        }
    }

    private LecturerInfoDTO mapToLecturerInfoDTO(Lecturer lecturer) {
        return LecturerInfoDTO.builder()
                .id(lecturer.getId())
                .lecturerId(lecturer.getLecturerId())
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

    // @Transactional
    // public List<RequestFromLecturer<?>> getLecturerRequests() {
    // try {
    // List<RequestFromLecturer<?>> requests = new ArrayList<>();

    // // Parallel processing for different request types
    // List<RequestFromLecturer<?>> createRequests = getCreateRequests();
    // List<RequestFromLecturer<?>> updateRequests = getUpdateRequests();

    // requests.addAll(createRequests);
    // requests.addAll(updateRequests);

    // // Sort by date (newest first) - use parallel stream for large collections
    // if (requests.size() > 100) {
    // requests = requests.parallelStream()
    // .sorted((r1, r2) -> r2.getDate().compareTo(r1.getDate()))
    // .collect(Collectors.toList());
    // } else {
    // requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
    // }

    // return requests;
    // } catch (Exception e) {
    // throw new RuntimeException("Error fetching lecturer requests.", e);
    // }
    // }

    @Transactional
    public List<RequestFromLecturer<?>> getDegreeRequests() {
        try {
            List<RequestFromLecturer<?>> requests = new ArrayList<>();

            // Create requests
            List<Degree> degrees = degreeRepository.findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            requests.addAll(degrees.stream()
                    .map(degree -> RequestFromLecturer.<DegreeDTO>builder()
                            .content(degreeMapper.toDTO(degree))
                            .lecturerInfo(Mapper.mapToLecturerInfoDTO(degree.getLecturer()))
                            .type(RequestLecturerType.BC)
                            .label(RequestLabel.Create)
                            .date(degree.getUpdatedAt())
                            .build())
                    .toList());

            // Update requests
            List<DegreeUpdateDTO> degreeUpdates = getPendingDegreeUpdates();
            if (!degreeUpdates.isEmpty()) {
                // Batch load lecturers to avoid N+1 queries
                Set<UUID> lecturerIds = degreeUpdates.stream()
                        .map(dto -> dto.getLecturer().getId())
                        .collect(Collectors.toSet());

                Map<UUID, Lecturer> lecturerMap = lecturerRepository.findAllByIdWithUser(lecturerIds)
                        .stream()
                        .collect(Collectors.toMap(Lecturer::getId, Function.identity()));

                requests.addAll(degreeUpdates.stream()
                        .map(degreeUpdate -> {
                            Lecturer lecturer = lecturerMap.get(degreeUpdate.getLecturer().getId());
                            if (lecturer == null) {
                                throw new IllegalArgumentException("Lecturer not found: " +
                                        degreeUpdate.getLecturer().getId());
                            }
                            return RequestFromLecturer.<DegreeUpdateDTO>builder()
                                    .content(degreeUpdate)
                                    .lecturerInfo(Mapper.mapToLecturerInfoDTO(lecturer))
                                    .type(RequestLecturerType.BC)
                                    .label(RequestLabel.Update)
                                    .date(degreeUpdate.getUpdate().getUpdatedAt())
                                    .build();
                        })
                        .toList());
            }
            requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
            return requests;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching degree requests.", e);
        }
    }

    @Transactional
    public List<RequestFromLecturer<?>> getCertificationRequests() {
        try {
            List<RequestFromLecturer<?>> requests = new ArrayList<>();

            // Create requests
            List<Certification> certifications = certificationRepository
                    .findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            requests.addAll(certifications.stream()
                    .map(certification -> RequestFromLecturer.<CertificationDTO>builder()
                            .content(certificationMapper.toDTO(certification))
                            .lecturerInfo(Mapper.mapToLecturerInfoDTO(certification.getLecturer()))
                            .type(RequestLecturerType.CC)
                            .label(RequestLabel.Create)
                            .date(certification.getUpdatedAt())
                            .build())
                    .toList());

            // Update requests
            List<CertificationUpdateDTO> certificationUpdates = getPendingCertificationUpdates();
            if (!certificationUpdates.isEmpty()) {
                Set<UUID> lecturerIds = certificationUpdates.stream()
                        .map(dto -> dto.getLecturer().getId())
                        .collect(Collectors.toSet());

                Map<UUID, Lecturer> lecturerMap = lecturerRepository.findAllByIdWithUser(lecturerIds)
                        .stream()
                        .collect(Collectors.toMap(Lecturer::getId, Function.identity()));

                requests.addAll(certificationUpdates.stream()
                        .map(certificationUpdate -> {
                            Lecturer lecturer = lecturerMap.get(certificationUpdate.getLecturer().getId());
                            if (lecturer == null) {
                                throw new IllegalArgumentException(
                                        "Lecturer not found: " + certificationUpdate.getLecturer().getId());
                            }
                            return RequestFromLecturer.<CertificationUpdateDTO>builder()
                                    .content(certificationUpdate)
                                    .lecturerInfo(Mapper.mapToLecturerInfoDTO(lecturer))
                                    .type(RequestLecturerType.CC)
                                    .label(RequestLabel.Update)
                                    .date(certificationUpdate.getUpdate().getUpdatedAt())
                                    .build();
                        })
                        .toList());
            }

            requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
            return requests;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching certification requests.", e);
        }
    }

    @Transactional
    public List<RequestFromLecturer<?>> getAttendedCourseRequests() {
        try {
            List<RequestFromLecturer<?>> requests = new ArrayList<>();

            // Create requests
            List<AttendedTrainingCourse> attendedCourses = attendedTrainingCourseRepository
                    .findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            requests.addAll(attendedCourses.stream()
                    .map(course -> RequestFromLecturer.<AttendedTrainingCourseDTO>builder()
                            .content(attendedTrainingCourseMapper.toDTO(course))
                            .lecturerInfo(Mapper.mapToLecturerInfoDTO(course.getLecturer()))
                            .type(RequestLecturerType.AC)
                            .label(RequestLabel.Create)
                            .date(course.getUpdatedAt())
                            .build())
                    .toList());

            // Update requests
            List<AttendedCourseUpdateDTO> attendedCourseUpdates = getPendingAttendedCourseUpdates();
            if (!attendedCourseUpdates.isEmpty()) {
                Set<UUID> lecturerIds = attendedCourseUpdates.stream()
                        .map(dto -> dto.getLecturer().getId())
                        .collect(Collectors.toSet());

                Map<UUID, Lecturer> lecturerMap = lecturerRepository.findAllByIdWithUser(lecturerIds)
                        .stream()
                        .collect(Collectors.toMap(Lecturer::getId, Function.identity()));

                requests.addAll(attendedCourseUpdates.stream()
                        .map(attendedCourseUpdate -> {
                            Lecturer lecturer = lecturerMap.get(attendedCourseUpdate.getLecturer().getId());
                            if (lecturer == null) {
                                throw new IllegalArgumentException(
                                        "Lecturer not found: " + attendedCourseUpdate.getLecturer().getId());
                            }
                            return RequestFromLecturer.<AttendedCourseUpdateDTO>builder()
                                    .content(attendedCourseUpdate)
                                    .lecturerInfo(Mapper.mapToLecturerInfoDTO(lecturer))
                                    .type(RequestLecturerType.AC)
                                    .label(RequestLabel.Update)
                                    .date(attendedCourseUpdate.getUpdate().getUpdatedAt())
                                    .build();
                        })
                        .toList());
            }

            requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
            return requests;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching attended course requests.", e);
        }
    }

    @Transactional
    public List<RequestFromLecturer<?>> getOwnedCourseRequests() {
        try {
            List<RequestFromLecturer<?>> requests = new ArrayList<>();

            // Create requests
            List<OwnedTrainingCourse> ownedCourses = ownedTrainingCourseRepository
                    .findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            requests.addAll(ownedCourses.stream()
                    .map(course -> RequestFromLecturer.<OwnedTrainingCourseDTO>builder()
                            .content(ownedTrainingCourseMapper.toDTO(course))
                            .lecturerInfo(Mapper.mapToLecturerInfoDTO(course.getLecturer()))
                            .type(RequestLecturerType.OC)
                            .label(RequestLabel.Create)
                            .date(course.getUpdatedAt())
                            .build())
                    .toList());

            // Update requests
            List<OwnedCourseUpdateDTO> ownedCourseUpdates = getPendingOwnedCourseUpdates();
            if (!ownedCourseUpdates.isEmpty()) {
                Set<UUID> lecturerIds = ownedCourseUpdates.stream()
                        .map(dto -> dto.getLecturer().getId())
                        .collect(Collectors.toSet());

                Map<UUID, Lecturer> lecturerMap = lecturerRepository.findAllByIdWithUser(lecturerIds)
                        .stream()
                        .collect(Collectors.toMap(Lecturer::getId, Function.identity()));

                requests.addAll(ownedCourseUpdates.stream()
                        .map(ownedCourseUpdate -> {
                            Lecturer lecturer = lecturerMap.get(ownedCourseUpdate.getLecturer().getId());
                            if (lecturer == null) {
                                throw new IllegalArgumentException(
                                        "Lecturer not found: " + ownedCourseUpdate.getLecturer().getId());
                            }
                            return RequestFromLecturer.<OwnedCourseUpdateDTO>builder()
                                    .content(ownedCourseUpdate)
                                    .lecturerInfo(Mapper.mapToLecturerInfoDTO(lecturer))
                                    .type(RequestLecturerType.OC)
                                    .label(RequestLabel.Update)
                                    .date(ownedCourseUpdate.getUpdate().getUpdatedAt())
                                    .build();
                        })
                        .toList());
            }

            requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
            return requests;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching owned course requests.", e);
        }
    }

    @Transactional
    public List<RequestFromLecturer<?>> getResearchProjectRequests() {
        try {
            List<RequestFromLecturer<?>> requests = new ArrayList<>();

            // Create requests
            List<ResearchProject> researchProjects = researchProjectRepository
                    .findByStatusWithApprovedLecturer(PendingStatus.PENDING);
            requests.addAll(researchProjects.stream()
                    .map(project -> RequestFromLecturer.<ResearchProjectDTO>builder()
                            .content(researchProjectMapper.toDTO(project))
                            .lecturerInfo(Mapper.mapToLecturerInfoDTO(project.getLecturer()))
                            .type(RequestLecturerType.RP)
                            .label(RequestLabel.Create)
                            .date(project.getUpdatedAt())
                            .build())
                    .toList());

            // Update requests
            List<ResearchProjectUpdateDTO> researchProjectUpdates = getPendingResearchProjectUpdates();
            if (!researchProjectUpdates.isEmpty()) {
                Set<UUID> lecturerIds = researchProjectUpdates.stream()
                        .map(dto -> dto.getLecturer().getId())
                        .collect(Collectors.toSet());

                Map<UUID, Lecturer> lecturerMap = lecturerRepository.findAllByIdWithUser(lecturerIds)
                        .stream()
                        .collect(Collectors.toMap(Lecturer::getId, Function.identity()));

                requests.addAll(researchProjectUpdates.stream()
                        .map(researchProjectUpdate -> {
                            Lecturer lecturer = lecturerMap.get(researchProjectUpdate.getLecturer().getId());
                            if (lecturer == null) {
                                throw new IllegalArgumentException(
                                        "Lecturer not found: " + researchProjectUpdate.getLecturer().getId());
                            }
                            return RequestFromLecturer.<ResearchProjectUpdateDTO>builder()
                                    .content(researchProjectUpdate)
                                    .lecturerInfo(Mapper.mapToLecturerInfoDTO(lecturer))
                                    .type(RequestLecturerType.RP)
                                    .label(RequestLabel.Update)
                                    .date(researchProjectUpdate.getUpdate().getUpdatedAt())
                                    .build();
                        })
                        .toList());
            }

            requests.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
            return requests;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching research project requests.", e);
        }
    }

    @Transactional
    public List<CourseInfoDTO> getAllCourses() {
        List<CourseInfoDTO> courseInfoDTOS = new ArrayList<>();
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            List<CourseMemberDTO> members = course.getCourseLecturers().stream()
                    .filter(cl -> cl.getLecturer() != null && cl.getLecturer().getStatus() == PendingStatus.APPROVED)
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

    @Transactional
    public void deleteCourse(IdRequest req) {
        if (req == null || req.getId() == null) {
            throw new IllegalArgumentException("ID không được để trống.");
        }
        try {

            Course course = courseRepository.findById(req.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với ID: " + req.getId()));
            courseRepository.delete(course);
            courseRepository.flush();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ID không hợp lệ.");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa khóa học: " + e.getMessage(), e);
        }
    }

}
