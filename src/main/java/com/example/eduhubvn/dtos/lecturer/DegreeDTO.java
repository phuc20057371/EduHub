package com.example.eduhubvn.dtos.lecturer;

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
public class DegreeDTO {
    private Integer id;
    private String referenceId;
    private String name;
    private String major;
    private String institution;
    private Integer startYear;
    private Integer graduationYear;
    private String level;
    private String url;
    private String description;

    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
