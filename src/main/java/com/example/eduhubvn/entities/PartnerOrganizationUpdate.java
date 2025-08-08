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
@Table(name = "partner_organization_update")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerOrganizationUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;



    @OneToOne
    @JoinColumn(name = "partner_organization_id", nullable = false)
    private PartnerOrganization partnerOrganization;

    @Column(name = "business_registration_number", unique = true, nullable = false)
    private String businessRegistrationNumber;
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
