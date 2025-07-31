package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "education_institution_update")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationInstitutionUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "education_institution_id", nullable = false)
    private EducationInstitution educationInstitution;

    @Column(name = "institution_name")
    private String institutionName;
    @Column(name = "institution_type")
    @Enumerated(EnumType.STRING)
    private EducationInstitutionType institutionType;

    @Column(name = "phone_number")
    private String phoneNumber;
    private String website;
    private String address;
    @Column(name = "representative_name")
    private String representativeName;
    private String position;
    private String description;
    @Column(name ="logo_url")
    private String logoUrl;
    @Column(name = "established_year")
    private Integer establishedYear;

    @Column(name = "admin_note")
    private String adminNote;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
