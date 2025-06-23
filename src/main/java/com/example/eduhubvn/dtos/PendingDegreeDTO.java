package com.example.eduhubvn.dtos;

import com.example.eduhubvn.entities.PendingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingDegreeDTO {
    private Integer id;
    private String name;
    private String major;
    private String institution;
    private Integer startYear;
    private Integer graduationYear;
    private String level;
    private String url;
    private String referenceID;
    private String reqNo;
    private String description;

    private Integer originalId;
    private PendingStatus status;
    private String reason;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reviewedAt;
}
