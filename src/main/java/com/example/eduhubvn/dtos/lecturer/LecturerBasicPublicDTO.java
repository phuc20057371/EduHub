package com.example.eduhubvn.dtos.lecturer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.eduhubvn.entities.AcademicRank;
import com.example.eduhubvn.entities.PendingStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LecturerBasicPublicDTO {
    private UUID id;
    private String fullName;
    private Boolean gender;
    private String bio;
    private String avatarUrl;
    private AcademicRank academicRank;
    private String specialization;
    private Integer experienceYears;
    private String jobField;
    private Double rating;

    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DegreeDTO> degrees;
    private List<CertificationDTO> certifications;

}
