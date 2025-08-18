package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.edu.InstitutionInfoDTO;
import com.example.eduhubvn.dtos.edu.request.EduInsUpdateReq;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.repositories.EducationInstitutionRepository;
import com.example.eduhubvn.repositories.EducationInstitutionUpdateRepository;
import com.example.eduhubvn.repositories.LecturerRepository;
import com.example.eduhubvn.ulti.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationInstitutionService {
    private final EducationInstitutionRepository educationInstitutionRepository;
    private final EducationInstitutionUpdateRepository educationInstitutionUpdateRepository;

    private final EducationInstitutionMapper educationInstitutionMapper;
    private final LecturerRepository lecturerRepository;

    /// GET
    @Transactional
    public List<EducationInstitutionPendingDTO> getPendingEducationInstitutionUpdates() {
        try {
            List<EducationInstitutionUpdate> pendingUpdates =
                    educationInstitutionUpdateRepository.findByStatus(PendingStatus.PENDING);

            return pendingUpdates.stream()
                    .map(update -> {
                        EducationInstitution institution = update.getEducationInstitution();
                        EducationInstitutionDTO institutionDTO = educationInstitutionMapper.toDTO(institution);
                        EducationInstitutionUpdateDTO updateDTO = educationInstitutionMapper.toDTO(update);
                        return new EducationInstitutionPendingDTO(institutionDTO, updateDTO);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EducationInstitutionDTO createEduInsFromUser(EducationInstitutionReq req, User user) {
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
            return educationInstitutionMapper.toDTO(institution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EducationInstitutionDTO updateEduins(EducationInstitutionReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution institution = user.getEducationInstitution();
        if (institution == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        System.out.println(user);
        if (user.getRole()== Role.SCHOOL && institution.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Bạn không thể cập nhật thông tin khi đã được phê duyệt.");
        }
        try {
            educationInstitutionMapper.updateEntityFromRequest(req, institution);
            institution.setStatus(PendingStatus.PENDING);
            institution.setAdminNote("");
            educationInstitutionRepository.save(institution);
            educationInstitutionRepository.flush();
            return educationInstitutionMapper.toDTO(institution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EduInsUpdateReq updateEduInsFromUser(EduInsUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution institution = user.getEducationInstitution();
        if (institution == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        try {
            Optional<EducationInstitutionUpdate> optionalUpdate =
                    educationInstitutionUpdateRepository.findByEducationInstitution(institution);
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
            return req;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý cập nhật: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<InstitutionInfoDTO> getPendingEducationInstitutionCreate() {
        try {
            List<EducationInstitution> pendingInstitutions =
                    educationInstitutionRepository.findByStatus(PendingStatus.PENDING);

            return pendingInstitutions.stream()
                    .map(Mapper::mapToInstitutionInfoDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EducationInstitutionDTO getPendingEduinsProfile(User user) {
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
        boolean isHidden = lecturer.isHidden();

        return LecturerInfoDTO.builder()
                .id(lecturer.getId())
                .citizenId(isHidden ? null : lecturer.getCitizenId())
                .email(isHidden
                        ? null
                        : (lecturer.getUser() != null ? lecturer.getUser().getEmail() : null))
                .phoneNumber(isHidden ? null : lecturer.getPhoneNumber())
                .fullName(lecturer.getFullName())
                .dateOfBirth(isHidden ? null : lecturer.getDateOfBirth())
                .gender(lecturer.getGender())
                .bio(lecturer.getBio())
                .address(isHidden ? null : lecturer.getAddress())
                .avatarUrl(lecturer.getAvatarUrl())
                .academicRank(lecturer.getAcademicRank())
                .specialization(lecturer.getSpecialization())
                .experienceYears(lecturer.getExperienceYears())
                .jobField(lecturer.getJobField())
                .hidden(isHidden)
                .adminNote(lecturer.getAdminNote())
                .status(lecturer.getStatus())
                .createdAt(lecturer.getCreatedAt())
                .updatedAt(lecturer.getUpdatedAt())
                .build();
    }
}
