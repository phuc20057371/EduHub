package com.example.eduhubvn.services;



import com.example.eduhubvn.dtos.*;

import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingLecturer;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        Optional<User> u = userRepository.findById(user.getId());
        if (u.isEmpty()) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        return u;

    }
    public UserProfileDTO getUserProfile() {
        User user = getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));

        // Lecturer
        Lecturer lecturer = user.getLecturer();
        LecturerDTO lecturerDTO = null;
        if (lecturer != null) {
            lecturerDTO = LecturerDTO.builder()
                    .id(lecturer.getId())
                    .fullName(lecturer.getFullName())
                    .citizenID(lecturer.getCitizenID())
                    .dateOfBirth(lecturer.getDateOfBirth())
                    .gender(lecturer.getGender())
                    .bio(lecturer.getBio())
                    .address(lecturer.getAddress())
                    .avatarUrl(lecturer.getAvatarUrl())
                    .academicRank(lecturer.getAcademicRank())
                    .specialization(lecturer.getSpecialization())
                    .experienceYears(lecturer.getExperienceYears())
                    .certifications(
                            lecturer.getCertifications().stream()
                                    .map(cert -> CertificationDTO.builder()
                                            .id(cert.getId())
                                            .name(cert.getName())
                                            .issuedBy(cert.getIssuedBy())
                                            .issueDate(cert.getIssueDate())
                                            .expiryDate(cert.getExpiryDate())
                                            .certificateUrl(cert.getCertificateUrl())
                                            .level(cert.getLevel())
                                            .description(cert.getDescription())
                                            .build()
                                    ).toList()
                    )
                    .degrees(
                            lecturer.getDegrees().stream()
                                    .map(deg -> DegreeDTO.builder()
                                            .id(deg.getId())
                                            .name(deg.getName())
                                            .major(deg.getMajor())
                                            .institution(deg.getInstitution())
                                            .startYear(deg.getStartYear())
                                            .graduationYear(deg.getGraduationYear())
                                            .level(deg.getLevel())
                                            .url(deg.getUrl())
                                            .referenceID(deg.getReferenceID())
                                            .reqNo(deg.getReqNo())
                                            .description(deg.getDescription())
                                            .build()
                                    ).toList()
                    )
                    .build();
        }

        PendingLecturer pending = user.getPendingLecturer();
        PendingLecturerDTO pendingDTO = null;

        if (pending != null) {
            pendingDTO = PendingLecturerDTO.builder()
                    .id(pending.getId())
                    .fullName(pending.getFullName())
                    .citizenID(pending.getCitizenID())
                    .dateOfBirth(pending.getDateOfBirth())
                    .gender(pending.getGender())
                    .bio(pending.getBio())
                    .address(pending.getAddress())
                    .avatarUrl(pending.getAvatarUrl())
                    .academicRank(pending.getAcademicRank())
                    .specialization(pending.getSpecialization())
                    .experienceYears(pending.getExperienceYears())
                    .status(pending.getStatus())
                    .response(pending.getResponse())
                    .createdAt(pending.getCreatedAt())
                    .updatedAt(pending.getUpdatedAt())
                    .pendingCertifications(
                            pending.getPendingCertifications().stream()
                                    .map(pc -> PendingCertificationDTO.builder()
                                            .id(pc.getId())
                                            .name(pc.getName())
                                            .issuedBy(pc.getIssuedBy())
                                            .issueDate(pc.getIssueDate())
                                            .expiryDate(pc.getExpiryDate())
                                            .certificateUrl(pc.getCertificateUrl())
                                            .level(pc.getLevel())
                                            .description(pc.getDescription())
                                            .originalId(pc.getOriginalId())
                                            .status(pc.getStatus())
                                            .reason(pc.getReason())
                                            .submittedAt(pc.getSubmittedAt())
                                            .updatedAt(pc.getUpdatedAt())
                                            .reviewedAt(pc.getReviewedAt())
                                            .build()
                                    ).toList()
                    )
                    .pendingDegrees(
                            pending.getPendingDegrees().stream()
                                    .map(pd -> PendingDegreeDTO.builder()
                                            .id(pd.getId())
                                            .name(pd.getName())
                                            .major(pd.getMajor())
                                            .institution(pd.getInstitution())
                                            .startYear(pd.getStartYear())
                                            .graduationYear(pd.getGraduationYear())
                                            .level(pd.getLevel())
                                            .url(pd.getUrl())
                                            .referenceID(pd.getReferenceID())
                                            .reqNo(pd.getReqNo())
                                            .description(pd.getDescription())
                                            .originalId(pd.getOriginalId())
                                            .status(pd.getStatus())
                                            .reason(pd.getReason())
                                            .submittedAt(pd.getSubmittedAt())
                                            .updatedAt(pd.getUpdatedAt())
                                            .reviewedAt(pd.getReviewedAt())
                                            .build()
                                    ).toList()
                    )
                    .build();

        }

        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .lecturer(lecturerDTO)
                .pendingLecturer(pendingDTO)
                .build();
    }

}
