package com.example.eduhubvn.dtos.lecturer.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DegreeCreateReq {
    private String referenceId;
    private String name;
    private String major;
    private String institution;
    private Integer startYear;
    private Integer graduationYear;
    private String level;
    private String url;
    private String description;
}
