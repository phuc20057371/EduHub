package com.example.eduhubvn.dtos.lecturer.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationUpdateReq {
    private UUID id;
    private String referenceId;
    private String name;
    private String issuedBy;
    private LocalDate issueDate;
    private LocalDate  expiryDate;
    private String certificateUrl;
    private String level;
    private String description;
}
