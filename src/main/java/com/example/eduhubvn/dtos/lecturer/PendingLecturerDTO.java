package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.PendingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
