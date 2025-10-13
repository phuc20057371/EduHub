package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.AcademicRank;
import com.example.eduhubvn.entities.PendingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LecturerInfoDTO {
    private UUID id;
    private String lecturerId;
    private String citizenId;
    private String email;
    private String phoneNumber;
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Boolean gender;
    private String bio;
    private String address;
    private String avatarUrl;
    private AcademicRank academicRank;
    private String specialization;
    private Integer experienceYears;
    private String jobField;

    private boolean hidden;

    private String adminNote;
    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
