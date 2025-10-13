package com.example.eduhubvn.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "training_program")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProgram {
    // system fields
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL)
    private List<TrainingUnit> units;

    @OneToOne
    @JoinColumn(name = "training_program_request_id")
    private TrainingProgramRequest trainingProgramRequest;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "partner_organization_id")
    private PartnerOrganization partnerOrganization;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
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

    private Integer maxStudents;
    private Integer minStudents;
    private String openingCondition;
    private String equipmentRequirement;
    private String classroomLink;

    private String targetAudience;
    @Column(columnDefinition = "TEXT")
    private String requirements;

    private String scale;

    private BigDecimal listedPrice;
    private BigDecimal internalPrice;
    private BigDecimal publicPrice;
    private boolean isPriceVisible;

    private String bannerUrl;
    private String contentUrl;

    private String syllabusFileUrl;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "training_program_tags", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    private String entryRequirements;
    private String learningOutcomes;

    private String completionCertificateType;
    private String certificateIssuer;

    private String title;
    private String subTitle;
    @Column(columnDefinition = "TEXT")
    private String shortDescription;
    private String learningObjectives;
    private String targetLearners;

    private Double rating;


}
