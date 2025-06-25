package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingLecturerRequest {
    private String citizenID;
    private String fullName;
    private Date dateOfBirth;
    private Boolean gender;
    private String bio;
    private String address;
    private String avatarUrl;
    private String academicRank;
    private String specialization;
    private Integer experienceYears;

    private List<PendingDegreeRequest> pendingDegrees;
    private List<PendingCertificationRequest> pendingCertifications;
}
