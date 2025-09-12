package com.example.eduhubvn.dtos.admin.request;

import com.example.eduhubvn.entities.EducationInstitutionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInstitutionFromAdminRequest {
    private String email;
    private String password;

    private String businessRegistrationNumber;
    private String institutionName;
    private EducationInstitutionType institutionType;
    private String phoneNumber;
    private String website;
    private String address;
    private String representativeName;
    private String position;
    private String description;
    private String logoUrl;
    private Integer establishedYear;

}
