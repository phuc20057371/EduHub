package com.example.eduhubvn.dtos.partner;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PendingPartnerOrganizationDTO {
    private Integer id;
    private String organizationName;
    private String industry;
    private String taxCode;
    private String phoneNumber;
    private String website;
    private String address;
    private String representativeName;
    private String position;
    private String description;
    private String logoUrl;
    private Integer establishedYear;
    private String reason;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
