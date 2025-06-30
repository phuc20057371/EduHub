package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.ProjectType;
import com.example.eduhubvn.entities.Scale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingResearchProjectDTO {
    private Integer id;
    private Integer originalId;
    private String title;
    private String researchArea;
    private Scale scale;
    private Date startDate;
    private Date endDate;
    private Double foundingAmount;
    private String foundingSource;
    private ProjectType projectType;
    private String roleInProject;
    private String publishedUrl;
    private String status;
    private String description;
    private PendingStatus pendingStatus;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer lecturerId;
}

