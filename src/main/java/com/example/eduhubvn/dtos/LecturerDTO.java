package com.example.eduhubvn.dtos;

import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LecturerDTO {
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

    private List<CertificationDTO> certifications;
    private List<DegreeDTO> degrees;
}
