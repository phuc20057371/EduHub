package com.example.eduhubvn.dtos.lecturer.pendingCourse;


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
public class PendingResearchProjectRequest {
    private Integer id;
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

}
