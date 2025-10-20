package com.example.eduhubvn.dtos.program;

import java.util.UUID;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.entities.PendingStatus;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramRequestDTO {

    private UUID id;

    private PartnerOrganizationDTO partnerOrganization;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;

    private String title;
    private String description;
    private String fileUrl;

    private String createdAt;
    private String updatedAt;

}
