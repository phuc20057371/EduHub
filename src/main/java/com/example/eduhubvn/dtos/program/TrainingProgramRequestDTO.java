package com.example.eduhubvn.dtos.program;

import java.util.UUID;

import com.example.eduhubvn.dtos.partner.PartnerDTO;
import com.example.eduhubvn.enums.PendingStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramRequestDTO {

    private UUID id;

    private PartnerDTO partnerOrganization;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;

    private String title;
    private String description;
    private String fileUrl;

    private String createdAt;
    private String updatedAt;

}
