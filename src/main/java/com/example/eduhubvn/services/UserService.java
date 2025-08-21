package com.example.eduhubvn.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.entities.AcademicRank;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.repositories.LecturerRepository;
import com.example.eduhubvn.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final LecturerMapper lecturerMapper;
    private final EducationInstitutionMapper educationInstitutionMapper;
    private final PartnerOrganizationMapper partnerOrganizationMapper;
    private final LecturerRepository lecturerRepository;

    // public Optional<User> getCurrentUser() {
    // Object principal =
    // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // if (!(principal instanceof User user)) {
    // throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
    // }
    // Optional<User> u = userRepository.findById(user.getId());
    // if (u.isEmpty()) {
    // throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
    // }
    // return u;
    // }

    public UserProfileDTO getCurrentUserProfile(User user) {
        try {
            return UserProfileDTO.builder()
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

            List<Lecturer> lecturers = userRepository.findByAcademicRankAndSpecializationContaining(rank,
                    specialization);
            return lecturers.stream()
                    .map(lecturerMapper::toDTO)
                    .toList();
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
