package com.example.eduhubvn.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturerResponse {
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

    private List<CertificationResponse> certifications;
    private List<DegreeResponse> degrees;
}
