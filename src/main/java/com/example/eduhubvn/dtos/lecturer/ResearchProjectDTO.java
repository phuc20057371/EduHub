package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.ProjectType;
import com.example.eduhubvn.entities.Scale;
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
public class ResearchProjectDTO {
    private Integer id;
    private String title;
    private String researchArea;
    private Scale scale;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double foundingAmount;
    private String foundingSource;
    private ProjectType projectType;
    private String roleInProject;
    private String publishedUrl;
    private String status;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
