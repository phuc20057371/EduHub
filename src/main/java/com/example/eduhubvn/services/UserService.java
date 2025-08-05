package com.example.eduhubvn.services;



import com.example.eduhubvn.dtos.*;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.*;
import com.example.eduhubvn.repositories.*;
import com.example.eduhubvn.ulti.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final LecturerMapper lecturerMapper;
    private final EducationInstitutionMapper educationInstitutionMapper;
    private final PartnerOrganizationMapper partnerOrganizationMapper;
    private final CertificationRepository certificationRepository;
    private final DegreeRepository degreeRepository;
    private final CertificationMapper certificationMapper;
    private final DegreeMapper degreeMapper;
    private final LecturerRepository lecturerRepository;


//    public Optional<User> getCurrentUser() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (!(principal instanceof User user)) {
//            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
//        }
//        Optional<User> u = userRepository.findById(user.getId());
//        if (u.isEmpty()) {
//            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
//        }
//        return u;
//    }

    public UserProfileDTO getCurrentUserProfile(User user) {
        try {
            return  UserProfileDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .role(String.valueOf(user.getRole()))
                    .lastLogin(user.getLastLogin())
                    .lecturer(lecturerMapper.toDTO(user.getLecturer()))
                    .educationInstitution(educationInstitutionMapper.toDTO(user.getEducationInstitution()))
                    .partnerOrganization(partnerOrganizationMapper.toDTO(user.getPartnerOrganization()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public List<LecturerDTO> findLecturers(String academicRank, String specialization) {
        try {
            AcademicRank rank = null;
            if (academicRank != null && !academicRank.trim().isEmpty()) {
                try {
                    rank = AcademicRank.valueOf(academicRank.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Handle invalid enum value - could log or ignore
                    rank = null;
                }
            }

            List<Lecturer> lecturers = userRepository.findByAcademicRankAndSpecializationContaining(rank, specialization);
            return lecturers.stream()
                    .map(lecturerMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            return degreeMapper.toDTO(degree);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            return certificationMapper.toDTO(certification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public Boolean checkCitizenIdExists(String citizenId) {
        if (citizenId == null || citizenId.trim().isEmpty()) {
            throw new IllegalArgumentException("Citizen ID không được để trống.");
        }
        Optional<Lecturer> lecturer = lecturerRepository.findByCitizenId(citizenId);
        return lecturer.isPresent();
    }
}
