package com.example.eduhubvn.entities;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "training_program_request")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProgramRequest {
    // system fields
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "trainingProgramRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TrainingProgram trainingProgram;

    @ManyToOne
    @JoinColumn(name = "partner_organization_id")
    private PartnerOrganization partnerOrganization;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;

    private String fileUrl;




}
