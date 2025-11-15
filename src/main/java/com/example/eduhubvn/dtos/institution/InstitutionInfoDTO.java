package com.example.eduhubvn.dtos.institution;

import com.example.eduhubvn.enums.EducationInstitutionType;
import com.example.eduhubvn.enums.PendingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionInfoDTO {
    private UUID id;
    private String email;

    private String businessRegistrationNumber;
    private String institutionName;
    private EducationInstitutionType institutionType;
    private String phoneNumber;
    private String website;
    private String address;
    private String representativeName;
    private String position;
    private String description;
    private String logoUrl;
    private Integer establishedYear;

    private String adminNote;
    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
