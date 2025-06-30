package com.example.eduhubvn.mapper;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionDTO;
import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.PendingEducationInstitution;


public class EducationInstitutionMapper {
    public static EducationInstitutionDTO toDTO(EducationInstitution entity) {
        if (entity == null) return null;

        EducationInstitutionDTO dto = new EducationInstitutionDTO();
        dto.setId(entity.getId());
        dto.setBusinessRegistrationNumber(entity.getBusinessRegistrationNumber());
        dto.setInstitutionName(entity.getInstitutionName());
        dto.setInstitutionType(entity.getInstitutionType());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setWebsite(entity.getWebsite());
        dto.setAddress(entity.getAddress());
        dto.setRepresentativeName(entity.getRepresentativeName());
        dto.setPosition(entity.getPosition());
        dto.setDescription(entity.getDescription());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setEstablishedYear(entity.getEstablishedYear());

        return dto;
    }
    public static PendingEducationInstitutionDTO toDTO(PendingEducationInstitution entity) {
        if (entity == null) return null;

        PendingEducationInstitutionDTO dto = new PendingEducationInstitutionDTO();
        dto.setId(entity.getId());
        dto.setBusinessRegistrationNumber(entity.getBusinessRegistrationNumber());
        dto.setInstitutionName(entity.getInstitutionName());
        dto.setInstitutionType(entity.getInstitutionType());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setWebsite(entity.getWebsite());
        dto.setAddress(entity.getAddress());
        dto.setRepresentativeName(entity.getRepresentativeName());
        dto.setPosition(entity.getPosition());
        dto.setDescription(entity.getDescription());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setEstablishedYear(entity.getEstablishedYear());
        dto.setStatus(entity.getStatus());
        dto.setReason(entity.getReason());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
