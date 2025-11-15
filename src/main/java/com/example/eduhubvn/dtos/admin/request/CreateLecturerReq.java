package com.example.eduhubvn.dtos.admin.request;

import java.time.LocalDate;

import com.example.eduhubvn.entities.AcademicRank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLecturerReq {
    private String email;
    private String password;

    private String citizenId;
    private String phoneNumber;
    private String fullName;
    private LocalDate dateOfBirth;
    private Boolean gender;
    private String bio;
    private String address;
    private String avatarUrl;
    private AcademicRank academicRank;
    private String specialization;
    private Integer experienceYears;
    private String jobField;
}
