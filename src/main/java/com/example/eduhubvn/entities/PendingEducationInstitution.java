package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString(exclude = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pending_education_institution")
public class PendingEducationInstitution {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "business_registration_number", length = 10, nullable = false)
    private String businessRegistrationNumber;

    @OneToOne
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

    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    private String reason;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
