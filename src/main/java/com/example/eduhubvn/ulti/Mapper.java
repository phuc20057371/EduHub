package com.example.eduhubvn.ulti;


import org.springframework.stereotype.Component;

import com.example.eduhubvn.dtos.institution.InstitutionInfoDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.dtos.partner.PartnerInfoDTO;
import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PartnerOrganization;


@Component
public class Mapper {


    public static LecturerInfoDTO mapToLecturerInfoDTO(Lecturer lecturer) {
        if (lecturer == null) {
            return null;
        }

        return LecturerInfoDTO.builder()
                .id(lecturer.getId())
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
                .adminNote(lecturer.getAdminNote())
                .status(lecturer.getStatus())
                .createdAt(lecturer.getCreatedAt())
                .updatedAt(lecturer.getUpdatedAt())
                .build();
    }

    public static InstitutionInfoDTO mapToInstitutionInfoDTO( EducationInstitution educationInstitution) {
        if (educationInstitution == null) {
            return null;
        }

        return InstitutionInfoDTO.builder()
                .id(educationInstitution.getId())
                .email(educationInstitution.getUser() != null ? educationInstitution.getUser().getEmail() : null)
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
                .adminNote(educationInstitution.getAdminNote())
                .status(educationInstitution.getStatus())
                .createdAt(educationInstitution.getCreatedAt())
                .updatedAt(educationInstitution.getUpdatedAt())
                .build();
    }

    public static PartnerInfoDTO mapToPartnerInfoDTO(PartnerOrganization partnerOrganization) {
        if (partnerOrganization == null) {
            return null;
        }

        return PartnerInfoDTO.builder()
                .id(partnerOrganization.getId())
                .email(partnerOrganization.getUser() != null ? partnerOrganization.getUser().getEmail() : null)
                .businessRegistrationNumber(partnerOrganization.getBusinessRegistrationNumber())
                .organizationName(partnerOrganization.getOrganizationName())
                .industry(partnerOrganization.getIndustry())
                .phoneNumber(partnerOrganization.getPhoneNumber())
                .website(partnerOrganization.getWebsite())
                .address(partnerOrganization.getAddress())
                .representativeName(partnerOrganization.getRepresentativeName())
                .position(partnerOrganization.getPosition())
                .description(partnerOrganization.getDescription())
                .logoUrl(partnerOrganization.getLogoUrl())
                .establishedYear(partnerOrganization.getEstablishedYear())
                .adminNote(partnerOrganization.getAdminNote())
                .status(partnerOrganization.getStatus())
                .createdAt(partnerOrganization.getCreatedAt())
                .updatedAt(partnerOrganization.getUpdatedAt())
                .build();
    }


}
