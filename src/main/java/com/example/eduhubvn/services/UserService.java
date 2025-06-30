package com.example.eduhubvn.services;



import com.example.eduhubvn.dtos.*;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import com.example.eduhubvn.entities.*;
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

        System.out.println(principal);
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
                    .citizenId(lecturer.getCitizenId())
                    .fullName(lecturer.getFullName())
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
                                            .referenceId(cert.getReferenceId())
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
                                            .referenceId(deg.getReferenceId())
                                            .name(deg.getName())
                                            .major(deg.getMajor())
                                            .institution(deg.getInstitution())
                                            .startYear(deg.getStartYear())
                                            .graduationYear(deg.getGraduationYear())
                                            .level(deg.getLevel())
                                            .url(deg.getUrl())
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
                    .citizenId(pending.getCitizenId())
                    .fullName(pending.getFullName())
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
                                            .referenceId(pc.getReferenceId())
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
                                            .referenceId(pd.getReferenceId())
                                            .name(pd.getName())
                                            .major(pd.getMajor())
                                            .institution(pd.getInstitution())
                                            .startYear(pd.getStartYear())
                                            .graduationYear(pd.getGraduationYear())
                                            .level(pd.getLevel())
                                            .url(pd.getUrl())
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
        PendingEducationInstitution pendingEducationInstitution = user.getPendingEducationInstitution();
        PendingEducationInstitutionDTO pendingEducationInstitutionDTO = null;
        if (pendingEducationInstitution != null) {
            pendingEducationInstitutionDTO = PendingEducationInstitutionDTO.builder()
                    .id(pendingEducationInstitution.getId())
                    .businessRegistrationNumber(pendingEducationInstitution.getBusinessRegistrationNumber())
                    .institutionName(pendingEducationInstitution.getInstitutionName())
                    .institutionType(pendingEducationInstitution.getInstitutionType())
                    .phoneNumber(pendingEducationInstitution.getPhoneNumber())
                    .website(pendingEducationInstitution.getWebsite())
                    .address(pendingEducationInstitution.getAddress())
                    .representativeName(pendingEducationInstitution.getRepresentativeName())
                    .position(pendingEducationInstitution.getPosition())
                    .description(pendingEducationInstitution.getDescription())
                    .logoUrl(pendingEducationInstitution.getLogoUrl())
                    .establishedYear(pendingEducationInstitution.getEstablishedYear())
                    .status(pendingEducationInstitution.getStatus())
                    .reason(pendingEducationInstitution.getReason())
                    .updatedAt(pendingEducationInstitution.getUpdatedAt())
                    .createdAt(pendingEducationInstitution.getCreatedAt())
                    .build();
        }
        EducationInstitution educationInstitution = user.getEducationInstitution();
        EducationInstitutionDTO educationInstitutionDTO = null;
        if (educationInstitution != null) {
            educationInstitutionDTO = EducationInstitutionDTO.builder()
                    .id(educationInstitution.getId())
                    .businessRegistrationNumber(educationInstitution.getBusinessRegistrationNumber())
                    .institutionName(educationInstitution.getInstitutionName())
                    .institutionType(educationInstitution.getInstitutionType())
                    .phoneNumber(educationInstitution.getPhoneNumber())
                    .website(educationInstitution.getWebsite())
                    .address(educationInstitution.getAddress())
                    .representativeName(educationInstitution.getRepresentativeName())
                    .position(educationInstitution.getPosition())
                    .description(educationInstitution.getDescription())
                    .logoUrl(educationInstitution.getLogoUrl())
                    .establishedYear(educationInstitution.getEstablishedYear())
                    .build();
        }
        PendingPartnerOrganization pendingOrg = user.getPendingPartnerOrganization();
        PendingPartnerOrganizationDTO pendingOrgDTO = null;
        if (pendingOrg != null) {
            pendingOrgDTO = PendingPartnerOrganizationDTO.builder()
                    .id(pendingOrg.getId())
                    .businessRegistrationNumber(pendingOrg.getBusinessRegistrationNumber())
                    .organizationName(pendingOrg.getOrganizationName())
                    .industry(pendingOrg.getIndustry())
                    .phoneNumber(pendingOrg.getPhoneNumber())
                    .website(pendingOrg.getWebsite())
                    .address(pendingOrg.getAddress())
                    .representativeName(pendingOrg.getRepresentativeName())
                    .position(pendingOrg.getPosition())
                    .description(pendingOrg.getDescription())
                    .logoUrl(pendingOrg.getLogoUrl())
                    .establishedYear(pendingOrg.getEstablishedYear())
                    .status(pendingOrg.getStatus().name())
                    .reason(pendingOrg.getReason())
                    .createdAt(pendingOrg.getCreatedAt())
                    .updatedAt(pendingOrg.getUpdatedAt())
                    .build();
        }

        PartnerOrganization partnerOrg = user.getPartnerOrganization();
        PartnerOrganizationDTO partnerOrgDTO = null;
        if (partnerOrg != null) {
            partnerOrgDTO = PartnerOrganizationDTO.builder()
                    .id(partnerOrg.getId())
                    .businessRegistrationNumber(partnerOrg.getBusinessRegistrationNumber())
                    .organizationName(partnerOrg.getOrganizationName())
                    .industry(partnerOrg.getIndustry())
                    .phoneNumber(partnerOrg.getPhoneNumber())
                    .website(partnerOrg.getWebsite())
                    .address(partnerOrg.getAddress())
                    .representativeName(partnerOrg.getRepresentativeName())
                    .position(partnerOrg.getPosition())
                    .description(partnerOrg.getDescription())
                    .logoUrl(partnerOrg.getLogoUrl())
                    .establishedYear(partnerOrg.getEstablishedYear())
                    .build();
        }

        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .lastLogin(user.getLastLogin())
                .lecturer(lecturerDTO)
                .pendingLecturer(pendingDTO)
                .pendingEducationInstitution(pendingEducationInstitutionDTO)
                .educationInstitution(educationInstitutionDTO)
                .pendingPartnerOrganization(pendingOrgDTO)
                .partnerOrganization(partnerOrgDTO)
                .build();
    }

}
