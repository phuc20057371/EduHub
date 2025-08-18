package com.example.eduhubvn.dtos.lecturer.request;

import com.example.eduhubvn.entities.AcademicRank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerReq {
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
