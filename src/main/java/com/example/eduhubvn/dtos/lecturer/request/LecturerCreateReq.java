package com.example.eduhubvn.dtos.lecturer.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.example.eduhubvn.enums.AcademicRank;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerCreateReq {
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
