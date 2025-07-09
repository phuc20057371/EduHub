package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.PendingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LecturerDTO {
    private Integer id;
    private String citizenId;
    private String phoneNumber;
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Boolean gender;
    private String bio;
    private String address;
    private String avatarUrl;
    private String academicRank;
    private String specialization;
    private Integer experienceYears;

    private String adminNote;
    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
