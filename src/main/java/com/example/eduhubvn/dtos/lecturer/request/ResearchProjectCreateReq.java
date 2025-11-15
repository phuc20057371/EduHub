package com.example.eduhubvn.dtos.lecturer.request;

import com.example.eduhubvn.enums.ProjectType;
import com.example.eduhubvn.enums.Scale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResearchProjectCreateReq {
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
    private String courseStatus;
    private String description;
}
