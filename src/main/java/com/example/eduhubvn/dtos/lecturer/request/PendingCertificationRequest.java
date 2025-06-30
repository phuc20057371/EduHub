package com.example.eduhubvn.dtos.lecturer.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingCertificationRequest {
    private Integer id;
    private String referenceId;
    private String name;
    private String issuedBy;
    private Date issueDate;
    private Date expiryDate;
    private String certificateUrl;
    private String level;
    private String description;
}
