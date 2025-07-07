package com.example.eduhubvn.dtos.partner;

import com.example.eduhubvn.entities.PendingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PartnerOrganizationUpdateDTO {
    private Integer id;
    private String organizationName;
    private String industry;
    private String phoneNumber;
    private String website;
    private String address;
    private String representativeName;
    private String position;
    private String description;
    private String logoUrl;
    private Integer establishedYear;

    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
