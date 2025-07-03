package com.example.eduhubvn.dtos.edu;

import com.example.eduhubvn.entities.EducationInstitutionType;
import com.example.eduhubvn.entities.PendingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EducationInstitutionDTO {
    private Integer id;
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

    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
