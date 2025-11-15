package com.example.eduhubvn.dtos.program.pub;

import java.util.UUID;

import com.example.eduhubvn.entities.AcademicRank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LecturerUnitPublicDTO {
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
}
