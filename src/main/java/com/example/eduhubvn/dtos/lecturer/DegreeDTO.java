package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eduhubvn.enums.PendingStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DegreeDTO {
    private UUID id;
    private String referenceId;
    private String name;
    private String major;
    private String institution;
    private Integer startYear;
    private Integer graduationYear;
    private String level;
    private String url;
    private String description;

    private String adminNote;
    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
