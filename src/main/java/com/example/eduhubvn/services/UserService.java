package com.example.eduhubvn.services;



import com.example.eduhubvn.dtos.*;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionDTO;
import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
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

        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .lastLogin(user.getLastLogin())
                .lecturer(mapLecturer(user.getLecturer()))
                .pendingLecturer(mapPendingLecturer(user.getPendingLecturer()))
                .pendingEducationInstitution(mapPendingEdu(user.getPendingEducationInstitution()))
                .educationInstitution(mapEdu(user.getEducationInstitution()))
                .pendingPartnerOrganization(mapPendingPartner(user.getPendingPartnerOrganization()))
                .partnerOrganization(mapPartner(user.getPartnerOrganization()))
                .build();
}

    private LecturerDTO mapLecturer(Lecturer lecturer) {
        if (lecturer == null) return null;
        return LecturerDTO.builder()
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
                .certifications(lecturer.getCertifications().stream().map(this::mapCert).toList())
                .degrees(lecturer.getDegrees().stream().map(this::mapDegree).toList())
                .build();
    }

    private CertificationDTO mapCert(Certification c) {
        return CertificationDTO.builder()
                .id(c.getId())
                .referenceId(c.getReferenceId())
                .name(c.getName())
                .issuedBy(c.getIssuedBy())
                .issueDate(c.getIssueDate())
                .expiryDate(c.getExpiryDate())
                .certificateUrl(c.getCertificateUrl())
                .level(c.getLevel())
                .description(c.getDescription())
                .build();
    }

    private DegreeDTO mapDegree(Degree d) {
        return DegreeDTO.builder()
                .id(d.getId())
                .referenceId(d.getReferenceId())
                .name(d.getName())
                .major(d.getMajor())
                .institution(d.getInstitution())
                .startYear(d.getStartYear())
                .graduationYear(d.getGraduationYear())
                .level(d.getLevel())
                .url(d.getUrl())
                .description(d.getDescription())
                .build();
    }

    private PendingLecturerDTO mapPendingLecturer(PendingLecturer p) {
        if (p == null) return null;
        return PendingLecturerDTO.builder()
                .id(p.getId())
                .citizenId(p.getCitizenId())
                .fullName(p.getFullName())
                .dateOfBirth(p.getDateOfBirth())
                .gender(p.getGender())
                .bio(p.getBio())
                .address(p.getAddress())
                .avatarUrl(p.getAvatarUrl())
                .academicRank(p.getAcademicRank())
                .specialization(p.getSpecialization())
                .experienceYears(p.getExperienceYears())
                .status(p.getStatus())
                .response(p.getResponse())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .pendingCertifications(p.getPendingCertifications().stream().map(this::mapPendingCert).toList())
                .pendingDegrees(p.getPendingDegrees().stream().map(this::mapPendingDegree).toList())
                .build();
    }

    private PendingCertificationDTO mapPendingCert(PendingCertification pc) {
        return PendingCertificationDTO.builder()
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
                .build();
    }

    private PendingDegreeDTO mapPendingDegree(PendingDegree pd) {
        return PendingDegreeDTO.builder()
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
                .build();
    }

    private PendingEducationInstitutionDTO mapPendingEdu(PendingEducationInstitution e) {
        if (e == null) return null;
        return PendingEducationInstitutionDTO.builder()
                .id(e.getId())
                .businessRegistrationNumber(e.getBusinessRegistrationNumber())
                .institutionName(e.getInstitutionName())
                .institutionType(e.getInstitutionType())
                .phoneNumber(e.getPhoneNumber())
                .website(e.getWebsite())
                .address(e.getAddress())
                .representativeName(e.getRepresentativeName())
                .position(e.getPosition())
                .description(e.getDescription())
                .logoUrl(e.getLogoUrl())
                .establishedYear(e.getEstablishedYear())
                .status(e.getStatus())
                .reason(e.getReason())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private EducationInstitutionDTO mapEdu(EducationInstitution e) {
        if (e == null) return null;
        return EducationInstitutionDTO.builder()
                .id(e.getId())
                .businessRegistrationNumber(e.getBusinessRegistrationNumber())
                .institutionName(e.getInstitutionName())
                .institutionType(e.getInstitutionType())
                .phoneNumber(e.getPhoneNumber())
                .website(e.getWebsite())
                .address(e.getAddress())
                .representativeName(e.getRepresentativeName())
                .position(e.getPosition())
                .description(e.getDescription())
                .logoUrl(e.getLogoUrl())
                .establishedYear(e.getEstablishedYear())
                .build();
    }

    private PendingPartnerOrganizationDTO mapPendingPartner(PendingPartnerOrganization p) {
        if (p == null) return null;
        return PendingPartnerOrganizationDTO.builder()
                .id(p.getId())
                .businessRegistrationNumber(p.getBusinessRegistrationNumber())
                .organizationName(p.getOrganizationName())
                .industry(p.getIndustry())
                .phoneNumber(p.getPhoneNumber())
                .website(p.getWebsite())
                .address(p.getAddress())
                .representativeName(p.getRepresentativeName())
                .position(p.getPosition())
                .description(p.getDescription())
                .logoUrl(p.getLogoUrl())
                .establishedYear(p.getEstablishedYear())
                .status(p.getStatus().name())
                .reason(p.getReason())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private PartnerOrganizationDTO mapPartner(PartnerOrganization p) {
        return PartnerOrganizationMapper.toDTO(p);
    }

}
