package com.example.eduhubvn.dtos;

import com.example.eduhubvn.entities.PendingCertification;
import com.example.eduhubvn.entities.PendingDegree;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingLecturerDTO {

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
    private PendingStatus status;
    private String response;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<PendingCertificationDTO> pendingCertifications;
    private List<PendingDegreeDTO> pendingDegrees;
}
