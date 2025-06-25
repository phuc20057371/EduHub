package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificationResponse {
    private String name;
    private String issuedBy;
    private Date issueDate;
    private Date expiryDate;
    private String certificateUrl;
    private String level;
    private String description;
}
