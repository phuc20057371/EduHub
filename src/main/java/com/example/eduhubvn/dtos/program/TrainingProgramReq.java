package com.example.eduhubvn.dtos.program;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.entities.TrainingProgramLevel;
import com.example.eduhubvn.entities.TrainingProgramMode;
import com.example.eduhubvn.entities.TrainingProgramStatus;
import com.example.eduhubvn.entities.TrainingProgramType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramReq {

    private TrainingProgramRequestDTO trainingProgramRequest;
    private PartnerOrganizationDTO partnerOrganization;

    // hidden fields
    private TrainingProgramStatus programStatus;
    private TrainingProgramMode programMode;
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
