package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingLecturerResponse {
    private Integer id;
    private String fullName;
    private String citizenID;
    private Date dateOfBirth;
    private Boolean gender;
    private String bio;
    private String address;
    private String avatarUrl;
    private String academicRank;
    private String specialization;
    private Integer experienceYears;
    private String status;
    private String response;

    private List<PendingDegreeResponse> pendingDegrees;
    private List<PendingCertificationResponse> pendingCertifications;
}
