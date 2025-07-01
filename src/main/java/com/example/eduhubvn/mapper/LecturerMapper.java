package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.dtos.lecturer.request.PendingLecturerUpdateResponse;
import com.example.eduhubvn.entities.*;

import java.util.stream.Collectors;


public class LecturerMapper {
    public static PendingLecturerDTO toPendingLecturerDTO(PendingLecturer entity) {
        if (entity == null) return null;

        PendingLecturerDTO dto = new PendingLecturerDTO();
        dto.setId(entity.getId());
        dto.setCitizenId(entity.getCitizenId());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setBio(entity.getBio());
        dto.setAddress(entity.getAddress());
        dto.setAvatarUrl(entity.getAvatarUrl());
        dto.setAcademicRank(entity.getAcademicRank());
        dto.setSpecialization(entity.getSpecialization());
        dto.setExperienceYears(entity.getExperienceYears());

        dto.setStatus(entity.getStatus());
        dto.setResponse(entity.getResponse());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        dto.setPendingDegrees(entity.getPendingDegrees() != null ?
                entity.getPendingDegrees().stream().map(degree -> {
                    PendingDegreeDTO d = new PendingDegreeDTO();
                    d.setId(degree.getId());
                    d.setReferenceId(degree.getReferenceId());
                    d.setName(degree.getName());
                    d.setMajor(degree.getMajor());
                    d.setInstitution(degree.getInstitution());
                    d.setStartYear(degree.getStartYear());
                    d.setGraduationYear(degree.getGraduationYear());
                    d.setLevel(degree.getLevel());
                    d.setUrl(degree.getUrl());
                    d.setDescription(degree.getDescription());

                    d.setOriginalId(degree.getOriginalId());
                    d.setReason(degree.getReason());
                    d.setStatus(degree.getStatus());
                    d.setSubmittedAt(degree.getSubmittedAt());
                    d.setUpdatedAt(degree.getUpdatedAt());
                    d.setReviewedAt(degree.getReviewedAt());
                    return d;
                }).collect(Collectors.toList())
                : null);

        dto.setPendingCertifications(entity.getPendingCertifications() != null ?
                entity.getPendingCertifications().stream().map(cert -> {
                    PendingCertificationDTO c = new PendingCertificationDTO();
                    c.setId(cert.getId());
                    c.setReferenceId(cert.getReferenceId());
                    c.setName(cert.getName());
                    c.setIssuedBy(cert.getIssuedBy());
                    c.setIssueDate(cert.getIssueDate());
                    c.setExpiryDate(cert.getExpiryDate());
                    c.setCertificateUrl(cert.getCertificateUrl());
                    c.setLevel(cert.getLevel());
                    c.setDescription(cert.getDescription());

                    c.setOriginalId(cert.getOriginalId());
                    c.setReason(cert.getReason());
                    c.setStatus(cert.getStatus());
                    c.setSubmittedAt(cert.getSubmittedAt());
                    c.setUpdatedAt(cert.getUpdatedAt());
                    c.setReviewedAt(cert.getReviewedAt());
                    return c;
                }).collect(Collectors.toList())
                : null);

        return dto;
    }


    public static LecturerDTO toLecturerDTO(Lecturer entity) {
        if (entity == null) return null;

        LecturerDTO dto = new LecturerDTO();
        dto.setId(entity.getId());
        dto.setCitizenId(entity.getCitizenId());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setBio(entity.getBio());
        dto.setAddress(entity.getAddress());
        dto.setAvatarUrl(entity.getAvatarUrl());
        dto.setAcademicRank(entity.getAcademicRank());
        dto.setSpecialization(entity.getSpecialization());
        dto.setExperienceYears(entity.getExperienceYears());

        if (entity.getCertifications() != null) {
            dto.setCertifications(
                    entity.getCertifications().stream().map(cert -> {
                        CertificationDTO c = new CertificationDTO();
                        c.setId(cert.getId());
                        c.setReferenceId(cert.getReferenceId());
                        c.setName(cert.getName());
                        c.setIssuedBy(cert.getIssuedBy());
                        c.setIssueDate(cert.getIssueDate());
                        c.setExpiryDate(cert.getExpiryDate());
                        c.setCertificateUrl(cert.getCertificateUrl());
                        c.setLevel(cert.getLevel());
                        c.setDescription(cert.getDescription());
                        return c;
                    }).collect(Collectors.toList())
            );
        }

        if (entity.getDegrees() != null) {
            dto.setDegrees(
                    entity.getDegrees().stream().map(degree -> {
                        DegreeDTO d = new DegreeDTO();
                        d.setId(degree.getId());
                        d.setReferenceId(degree.getReferenceId());
                        d.setName(degree.getName());
                        d.setMajor(degree.getMajor());
                        d.setInstitution(degree.getInstitution());
                        d.setStartYear(degree.getStartYear());
                        d.setGraduationYear(degree.getGraduationYear());
                        d.setLevel(degree.getLevel());
                        d.setUrl(degree.getUrl());
                        d.setDescription(degree.getDescription());
                        return d;
                    }).collect(Collectors.toList())
            );
        }

        return dto;
    }

    public static PendingOwnedTrainingCourseDTO pendingOwnedTrainingCourseDTO(PendingOwnedTrainingCourse course) {
        return PendingOwnedTrainingCourseDTO.builder()
                .id(course.getId())
                .originalId(course.getOriginalId())
                .title(course.getTitle())
                .topic(course.getTopic())
                .courseType(course.getCourseType())
                .scale(course.getScale())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .numberOfHour(course.getNumberOfHour())
                .location(course.getLocation())
                .status(course.getStatus())
                .description(course.getDescription())
                .courseUrl(course.getCourseUrl())
                .pendingStatus(course.getPendingStatus())
                .reason(course.getReason())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .lecturerId(course.getLecturer().getId())
                .build();
    }

        public static PendingAttendedTrainingCourseDTO mapToAttendedDTO(PendingAttendedTrainingCourse entity) {
            if (entity == null) return null;

            PendingAttendedTrainingCourseDTO dto = new PendingAttendedTrainingCourseDTO();
            dto.setId(entity.getId());
            dto.setOriginalId(entity.getOriginalId());
            dto.setTitle(entity.getTitle());
            dto.setTopic(entity.getTopic());
            dto.setOrganizer(entity.getOrganizer());
            dto.setCourseType(entity.getCourseType());
            dto.setScale(entity.getScale());
            dto.setStartDate(entity.getStartDate());
            dto.setEndDate(entity.getEndDate());
            dto.setNumberOfHour(entity.getNumberOfHour());
            dto.setLocation(entity.getLocation());
            dto.setDescription(entity.getDescription());
            dto.setCourseUrl(entity.getCourseUrl());
            dto.setPendingStatus(entity.getPendingStatus());
            dto.setReason(entity.getReason());
            dto.setCreatedAt(entity.getCreatedAt());
            dto.setUpdatedAt(entity.getUpdatedAt());

            if (entity.getLecturer() != null) {
                dto.setLecturerId(entity.getLecturer().getId());
            }

            return dto;
        }

    public static PendingResearchProjectDTO mapToResearchProjectDTO(PendingResearchProject entity) {
        if (entity == null) return null;

        PendingResearchProjectDTO dto = new PendingResearchProjectDTO();
        dto.setId(entity.getId());
        dto.setOriginalId(entity.getOriginalId());
        dto.setTitle(entity.getTitle());
        dto.setResearchArea(entity.getResearchArea());
        dto.setScale(entity.getScale());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setFoundingAmount(entity.getFoundingAmount());
        dto.setFoundingSource(entity.getFoundingSource());
        dto.setProjectType(entity.getProjectType());
        dto.setRoleInProject(entity.getRoleInProject());
        dto.setPublishedUrl(entity.getPublishedUrl());
        dto.setStatus(entity.getStatus());
        dto.setDescription(entity.getDescription());
        dto.setPendingStatus(entity.getPendingStatus());
        dto.setReason(entity.getReason());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getLecturer() != null) {
            dto.setLecturerId(entity.getLecturer().getId());
        }

        return dto;
    }

    public static PendingLecturerUpdateResponse toPendingLecturerUpdateResponse(PendingLecturerDTO dto) {
        if (dto == null) return null;

        PendingLecturerUpdateResponse response = new PendingLecturerUpdateResponse();
        response.setId(dto.getId());
        response.setCitizenId(dto.getCitizenId());
        response.setFullName(dto.getFullName());
        response.setDateOfBirth(dto.getDateOfBirth());
        response.setGender(dto.getGender());
        response.setBio(dto.getBio());
        response.setAddress(dto.getAddress());
        response.setAvatarUrl(dto.getAvatarUrl());
        response.setAcademicRank(dto.getAcademicRank());
        response.setSpecialization(dto.getSpecialization());
        response.setExperienceYears(dto.getExperienceYears());

        return response;
    }

    public static PendingCertificationDTO toPendingCertificationDTO(PendingCertification cert) {
        if (cert == null) return null;

        return PendingCertificationDTO.builder()
                .id(cert.getId())
                .referenceId(cert.getReferenceId())
                .name(cert.getName())
                .issuedBy(cert.getIssuedBy())
                .issueDate(cert.getIssueDate())
                .expiryDate(cert.getExpiryDate())
                .certificateUrl(cert.getCertificateUrl())
                .level(cert.getLevel())
                .description(cert.getDescription())
                .originalId(cert.getOriginalId())
                .status(cert.getStatus())
                .reason(cert.getReason())
                .submittedAt(cert.getSubmittedAt())
                .updatedAt(cert.getUpdatedAt())
                .reviewedAt(cert.getReviewedAt())
                .lecturerId(cert.getPendingLecturer() != null ? cert.getPendingLecturer().getId() : null)
                .build();
    }

    public static PendingDegreeDTO toPendingDegreeDTO(PendingDegree degree) {
        if (degree == null) return null;

        return PendingDegreeDTO.builder()
                .id(degree.getId())
                .referenceId(degree.getReferenceId())
                .name(degree.getName())
                .major(degree.getMajor())
                .institution(degree.getInstitution())
                .startYear(degree.getStartYear())
                .graduationYear(degree.getGraduationYear())
                .level(degree.getLevel())
                .url(degree.getUrl())
                .description(degree.getDescription())
                .originalId(degree.getOriginalId())
                .status(degree.getStatus())
                .reason(degree.getReason())
                .submittedAt(degree.getSubmittedAt())
                .updatedAt(degree.getUpdatedAt())
                .reviewedAt(degree.getReviewedAt())
                .build();
    }

    public static CertificationDTO toCertificationDTO(Certification cert) {
        if (cert == null) return null;

        return CertificationDTO.builder()
                .id(cert.getId())
                .referenceId(cert.getReferenceId())
                .name(cert.getName())
                .issuedBy(cert.getIssuedBy())
                .issueDate(cert.getIssueDate())
                .expiryDate(cert.getExpiryDate())
                .certificateUrl(cert.getCertificateUrl())
                .level(cert.getLevel())
                .description(cert.getDescription())
                .build();
    }

    public static DegreeDTO toDegreeDTO(Degree degree) {
        if (degree == null) return null;

        return DegreeDTO.builder()
                .id(degree.getId())
                .referenceId(degree.getReferenceId())
                .name(degree.getName())
                .major(degree.getMajor())
                .institution(degree.getInstitution())
                .startYear(degree.getStartYear())
                .graduationYear(degree.getGraduationYear())
                .level(degree.getLevel())
                .url(degree.getUrl())
                .description(degree.getDescription())
                .build();
    }

    public static OwnedTrainingCourseDTO toOwnedTrainingCourseDTO(OwnedTrainingCourse course) {
        if (course == null) return null;

        return OwnedTrainingCourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .topic(course.getTopic())
                .courseType(course.getCourseType())
                .scale(course.getScale())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .numberOfHour(course.getNumberOfHour())
                .status(course.getStatus())
                .location(course.getLocation())
                .description(course.getDescription())
                .courseUrl(course.getCourseUrl())
                .build();
    }

    public static AttendedTrainingCourseDTO toAttendedTrainingCourseDTO(AttendedTrainingCourse course) {
        if (course == null) return null;

        return AttendedTrainingCourseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .topic(course.getTopic())
                .organizer(course.getOrganizer())
                .courseType(course.getCourseType())
                .scale(course.getScale())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .numberOfHour(course.getNumberOfHour())
                .location(course.getLocation())
                .description(course.getDescription())
                .courseUrl(course.getCourseUrl())
                .build();
    }

    public static ResearchProjectDTO toResearchProjectDTO(ResearchProject project) {
        if (project == null) return null;

        return ResearchProjectDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .researchArea(project.getResearchArea())
                .scale(project.getScale())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .foundingAmount(project.getFoundingAmount())
                .foundingSource(project.getFoundingSource())
                .projectType(project.getProjectType())
                .roleInProject(project.getRoleInProject())
                .publishedUrl(project.getPublishedUrl())
                .status(project.getStatus())
                .description(project.getDescription())
                .build();
    }
}
