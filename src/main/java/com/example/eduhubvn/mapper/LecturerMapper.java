package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.lecturer.*;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingLecturer;

import java.util.stream.Collectors;


public class LecturerMapper {
    public static PendingLecturerResponse toPendingLecturerResponse(PendingLecturer entity) {
        if (entity == null) return null;

        PendingLecturerResponse dto = new PendingLecturerResponse();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setCitizenID(entity.getCitizenID());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setBio(entity.getBio());
        dto.setAddress(entity.getAddress());
        dto.setAvatarUrl(entity.getAvatarUrl());
        dto.setAcademicRank(entity.getAcademicRank());
        dto.setSpecialization(entity.getSpecialization());
        dto.setExperienceYears(entity.getExperienceYears());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setResponse(entity.getResponse());

        dto.setPendingDegrees(entity.getPendingDegrees() != null ?
                entity.getPendingDegrees().stream().map(degree -> {
                    PendingDegreeResponse d = new PendingDegreeResponse();
                    d.setName(degree.getName());
                    d.setMajor(degree.getMajor());
                    d.setInstitution(degree.getInstitution());
                    d.setStartYear(degree.getStartYear());
                    d.setGraduationYear(degree.getGraduationYear());
                    d.setLevel(degree.getLevel());
                    d.setUrl(degree.getUrl());
                    d.setReferenceID(degree.getReferenceID());
                    d.setReqNo(degree.getReqNo());
                    d.setDescription(degree.getDescription());
                    return d;
                }).collect(Collectors.toList())
                : null);

        dto.setPendingCertifications(entity.getPendingCertifications() != null ?
                entity.getPendingCertifications().stream().map(cert -> {
                    PendingCertificationResponse c = new PendingCertificationResponse();
                    c.setName(cert.getName());
                    c.setIssuedBy(cert.getIssuedBy());
                    c.setIssueDate(cert.getIssueDate());
                    c.setExpiryDate(cert.getExpiryDate());
                    c.setCertificateUrl(cert.getCertificateUrl());
                    c.setLevel(cert.getLevel());
                    c.setDescription(cert.getDescription());
                    return c;
                }).collect(Collectors.toList())
                : null);

        return dto;
    }


    public static LecturerResponse toLecturerResponse(Lecturer entity) {
        if (entity == null) return null;

        LecturerResponse dto = new LecturerResponse();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setCitizenID(entity.getCitizenID());
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
                        CertificationResponse c = new CertificationResponse();
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
                        DegreeResponse d = new DegreeResponse();
                        d.setName(degree.getName());
                        d.setMajor(degree.getMajor());
                        d.setInstitution(degree.getInstitution());
                        d.setStartYear(degree.getStartYear());
                        d.setGraduationYear(degree.getGraduationYear());
                        d.setLevel(degree.getLevel());
                        d.setUrl(degree.getUrl());
                        d.setReferenceID(degree.getReferenceID());
                        d.setDescription(degree.getDescription());
                        return d;
                    }).collect(Collectors.toList())
            );
        }

        return dto;
    }
}
