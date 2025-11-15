package com.example.eduhubvn.dtos.partner;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eduhubvn.enums.PendingStatus;

@Data
@Builder
public class PartnerUpdateDTO {
    private UUID id;
    private String businessRegistrationNumber;
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

    private String adminNote;
    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
