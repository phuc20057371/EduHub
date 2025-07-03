package com.example.eduhubvn.dtos.lecturer.request;

import com.example.eduhubvn.entities.PendingStatus;
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
    private String fullName;
    private LocalDate dateOfBirth;
    private Boolean gender;
    private String bio;
    private String address;
    private String avatarUrl;
    private String academicRank;
    private String specialization;
    private Integer experienceYears;
}
