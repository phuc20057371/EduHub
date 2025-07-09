package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "partner_organization")
@Data
@ToString(exclude = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerOrganization {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "business_registration_number", length = 10, nullable = false, unique = true)
    private String businessRegistrationNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "organization_name")
    private String organizationName;
    private String industry;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String website;
    private String address;
    @Column(name = "representative_name")
    private String representativeName;
    private String position;
    @Column(columnDefinition = "TEXT")
    private String description;
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
