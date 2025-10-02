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
    private final SubAdminService subAdminService;

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

    @Transactional
    public UserProfileDTO getCurrentUserProfile(User user) {
        try {
            // Load full user from database to get all lazy-loaded fields like subEmails
            User fullUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            UserProfileDTO.UserProfileDTOBuilder builder = UserProfileDTO.builder()
                    .id(fullUser.getId())
                    .email(fullUser.getEmail())
                    .role(String.valueOf(fullUser.getRole()))
                    .lastLogin(fullUser.getLastLogin())
                    .subEmails(fullUser.getSubEmails().stream().toList())
                    .lecturer(lecturerMapper.toDTO(fullUser.getLecturer()))
                    .educationInstitution(educationInstitutionMapper.toDTO(fullUser.getEducationInstitution()))
                    .partnerOrganization(partnerOrganizationMapper.toDTO(fullUser.getPartnerOrganization()));
            
            // If user is SUB_ADMIN, add their permissions
            if (fullUser.getRole() != null && fullUser.getRole().name().equals("SUB_ADMIN")) {
                List<String> permissions = subAdminService.getUserPermissions(fullUser.getId());
                builder.permissions(permissions);
            }
            
            return builder.build();
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
