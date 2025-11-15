
package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.MessageSocket;
import com.example.eduhubvn.dtos.MessageSocketType;
import com.example.eduhubvn.dtos.institution.InstitutionDTO;
import com.example.eduhubvn.dtos.institution.InstitutionPendingDTO;
import com.example.eduhubvn.dtos.institution.InstitutionUpdateDTO;
import com.example.eduhubvn.dtos.institution.InstitutionInfoDTO;
import com.example.eduhubvn.dtos.institution.InstitutionProfileDTO;
import com.example.eduhubvn.dtos.institution.request.InstitutionCreateReq;
import com.example.eduhubvn.dtos.institution.request.InstitutionUpdateReq;
import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.enums.PendingStatus;
import com.example.eduhubvn.enums.Role;
import com.example.eduhubvn.mapper.InstitutionMapper;
import com.example.eduhubvn.repositories.InstitutionRepository;
import com.example.eduhubvn.repositories.EducationInstitutionUpdateRepository;
import com.example.eduhubvn.repositories.LecturerRepository;
import com.example.eduhubvn.ulti.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository educationInstitutionRepository;
    private final EducationInstitutionUpdateRepository educationInstitutionUpdateRepository;

    private final InstitutionMapper educationInstitutionMapper;
    private final LecturerRepository lecturerRepository;

    private final SimpMessagingTemplate messagingTemplate;

    /// GET
    @Transactional
    public List<InstitutionPendingDTO> getPendingEducationInstitutionUpdates() {
        try {
            List<EducationInstitutionUpdate> pendingUpdates = educationInstitutionUpdateRepository
                    .findByStatus(PendingStatus.PENDING);

            return pendingUpdates.stream()
                    .filter(update -> update.getEducationInstitution().getStatus() == PendingStatus.APPROVED)
                    .map(update -> {
                        EducationInstitution institution = update.getEducationInstitution();
                        InstitutionDTO institutionDTO = educationInstitutionMapper.toDTO(institution);
                        InstitutionUpdateDTO updateDTO = educationInstitutionMapper.toDTO(update);
                        return new InstitutionPendingDTO(institutionDTO, updateDTO);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public InstitutionDTO createEduInsFromUser(InstitutionCreateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if (educationInstitutionRepository.existsByBusinessRegistrationNumber(req.getBusinessRegistrationNumber())) {
            throw new IllegalArgumentException("Số đăng ký kinh doanh đã tồn tại trong hệ thống.");
        }
        if (user.getEducationInstitution() != null) {
            throw new EntityNotFoundException("Đã có tài khoản");
        }
        try {
            EducationInstitution institution = educationInstitutionMapper.toEntity(req);
            institution.setUser(user);
            institution.setStatus(PendingStatus.PENDING);
            educationInstitutionRepository.save(institution);
            educationInstitutionRepository.flush();
            InstitutionDTO dto = educationInstitutionMapper.toDTO(institution);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.CREATE_INSTITUTION, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public InstitutionDTO updateEduins(InstitutionCreateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution institution = user.getEducationInstitution();
        if (institution == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }

        if (user.getRole() == Role.SCHOOL && institution.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Bạn không thể cập nhật thông tin khi đã được phê duyệt.");
        }
        try {
            educationInstitutionMapper.updateEntityFromRequest(req, institution);
            institution.setStatus(PendingStatus.PENDING);
            institution.setAdminNote("");
            educationInstitutionRepository.save(institution);
            educationInstitutionRepository.flush();
            InstitutionDTO dto = educationInstitutionMapper.toDTO(institution);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.UPDATE_INSTITUTION, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public InstitutionDTO updateEduInsFromUser(InstitutionUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution institution = user.getEducationInstitution();
        if (institution == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        try {
            Optional<EducationInstitutionUpdate> optionalUpdate = educationInstitutionUpdateRepository
                    .findByEducationInstitution(institution);
            EducationInstitutionUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                educationInstitutionMapper.updateUpdateFromRequest(req, update);
            } else {
                update = educationInstitutionMapper.toUpdate(req);
                update.setEducationInstitution(institution);
            }
            update.setStatus(PendingStatus.PENDING);
            educationInstitutionUpdateRepository.save(update);
            educationInstitutionUpdateRepository.flush();
            InstitutionDTO dto = educationInstitutionMapper.toDTO(institution);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.EDIT_INSTITUTION, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý cập nhật: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<InstitutionInfoDTO> getPendingEducationInstitutionCreate() {
        try {
            List<EducationInstitution> pendingInstitutions = educationInstitutionRepository
                    .findByStatus(PendingStatus.PENDING);

            return pendingInstitutions.stream()
                    .map(Mapper::mapToInstitutionInfoDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public InstitutionDTO getPendingEduinsProfile(User user) {
        try {
            EducationInstitution institution = user.getEducationInstitution();
            if (institution == null) {
                throw new EntityNotFoundException("Không có quyền truy cập");
            }
            return educationInstitutionMapper.toDTO(institution);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<LecturerInfoDTO> getLecturers(User user) {
        try {
            EducationInstitution institution = user.getEducationInstitution();
            if (institution == null) {
                throw new EntityNotFoundException("Không có quyền truy cập");
            }
            List<Lecturer> lecturers = lecturerRepository.findAll();
            return lecturers.stream()
                    .filter(lecturer -> lecturer.getStatus() == PendingStatus.APPROVED)
                    .map(this::mapToLecturerInfoDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LecturerInfoDTO mapToLecturerInfoDTO(Lecturer lecturer) {

        return LecturerInfoDTO.builder()
                .id(lecturer.getId())
                .citizenId(null)
                .email(null)
                .phoneNumber(null)
                .fullName(lecturer.getFullName())
                .dateOfBirth(lecturer.getDateOfBirth())
                .gender(lecturer.getGender())
                .bio(null)
                .address(null)
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

    public InstitutionProfileDTO getInstitutionProfile(User user) {
        try {
            EducationInstitution institution = user.getEducationInstitution();
            if (institution == null) {
                throw new EntityNotFoundException("Không có quyền truy cập");
            }
            return InstitutionProfileDTO.builder()
                    .institution(Mapper.mapToInstitutionInfoDTO(institution))
                    .institutionUpdate(educationInstitutionMapper.toDTO(institution.getInstitutionUpdate()))
                    .build();
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public InstitutionDTO updateInstitutionLogo(String logoUrl, User user) {
        if (logoUrl == null || logoUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL logo không được trống.");
        }
        EducationInstitution institution = user.getEducationInstitution();
        if (institution == null) {
            throw new IllegalStateException("Không tìm thấy thông tin trường học.");
        }
        try {
            institution.setLogoUrl(logoUrl.trim());
            educationInstitutionRepository.save(institution);
            educationInstitutionRepository.flush();
            return educationInstitutionMapper.toDTO(institution);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật logo: " + e.getMessage(), e);
        }
    }
}
