package com.example.eduhubvn.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.eduhubvn.enums.EducationInstitutionType;
import com.example.eduhubvn.enums.PendingStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "education_institution")
public class EducationInstitution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "business_registration_number", length = 10, nullable = false, unique = true)
    private String businessRegistrationNumber;

    @OneToOne(cascade = { CascadeType.REFRESH, CascadeType.DETACH }, orphanRemoval = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

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
    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "established_year")
    private Integer establishedYear;

    private String adminNote;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    private boolean hidden;

    @OneToOne(mappedBy = "educationInstitution", cascade = CascadeType.ALL, orphanRemoval = true)
    private EducationInstitutionUpdate institutionUpdate;

    @OneToMany(mappedBy = "educationInstitution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;
}
