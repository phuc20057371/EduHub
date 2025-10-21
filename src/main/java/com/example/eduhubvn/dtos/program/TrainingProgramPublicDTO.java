package com.example.eduhubvn.dtos.program;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.example.eduhubvn.entities.TrainingProgramMode;
import com.example.eduhubvn.entities.TrainingProgramType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramPublicDTO {

    private UUID id;

    private List<TrainingProgramUnitPublicDTO> units;

    private TrainingProgramMode programMode;
    private TrainingProgramType programType;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer durationHours;
    private Integer durationSessions;
    private String scheduleDetail;

    private String equipmentRequirement;
    private String classroomLink;

    private String targetAudience;
    private String requirements;

    private String scale;

    private BigDecimal publicPrice;
    private boolean isPriceVisible;

    private String bannerUrl;
    private String contentUrl;

    private Set<String> tags;

    private String learningOutcomes;

    private String completionCertificateType;
    private String certificateIssuer;

    private String title;
    private String subTitle;
    private String shortDescription;
    private String learningObjectives;

    private Double rating;
}
