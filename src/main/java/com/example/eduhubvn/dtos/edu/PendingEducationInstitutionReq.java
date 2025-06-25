package com.example.eduhubvn.dtos.edu;

import com.example.eduhubvn.entities.EducationInstitutionType;
import com.example.eduhubvn.entities.PendingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingEducationInstitutionReq {
    private String institutionName;
    private EducationInstitutionType institutionType;
    private String taxCode;
    private String phoneNumber;
    private String website;
    private String address;
    private String representativeName;
    private String position;
    private String description;
    private String logoUrl;
    private Integer establishedYear;
    private PendingStatus status;
}
