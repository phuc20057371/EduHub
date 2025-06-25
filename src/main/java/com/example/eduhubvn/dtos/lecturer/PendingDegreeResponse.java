package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingDegreeResponse {
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

}
