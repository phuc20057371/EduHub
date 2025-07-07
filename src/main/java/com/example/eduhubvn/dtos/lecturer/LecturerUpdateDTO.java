package com.example.eduhubvn.dtos.lecturer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerUpdateDTO {
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private Boolean gender;
    private String bio;
    private String address;
    private String avatarUrl;
    private String academicRank;
    private String specialization;
    private Integer experienceYears;
    private String adminNote;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
