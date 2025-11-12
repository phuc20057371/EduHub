package com.example.eduhubvn.dtos.program;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.entities.TrainingProgramLevel;
import com.example.eduhubvn.entities.TrainingProgramMode;
import com.example.eduhubvn.entities.TrainingProgramStatus;
import com.example.eduhubvn.entities.TrainingProgramType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramDTO {

    private UUID id;
    private String trainingProgramId;

    private List<TrainingUnitDTO> units;

    private TrainingProgramRequestDTO trainingProgramRequest;

    private UserProfileDTO user;

    private PartnerOrganizationDTO partnerOrganization;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // hidden fields
    @Enumerated(EnumType.STRING)
    private TrainingProgramStatus programStatus;

    @Enumerated(EnumType.STRING)
    private TrainingProgramMode programMode;

    @Enumerated(EnumType.STRING)
    private TrainingProgramType programType;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationHours;
    private Integer durationSessions;
    private String scheduleDetail;

    private TrainingProgramLevel programLevel;

    private Integer maxStudents;
    private Integer minStudents;
    private String openingCondition;
    private String equipmentRequirement;
    private String classroomLink;

    private String targetAudience;
    private String requirements;

    private String scale;

    private BigDecimal listedPrice;
    private BigDecimal internalPrice;
    private BigDecimal publicPrice;
    private boolean priceVisible;

    private String bannerUrl;
    private String contentUrl;

    private String syllabusFileUrl;

    private Set<String> tags;

    private String learningOutcomes;

    private String completionCertificateType;
    private String certificateIssuer;

    private String title;
    private String subTitle;
    private String shortDescription;
    private String description;
    private String learningObjectives;

    private String trialVideoUrl;

    private Double rating;

}
