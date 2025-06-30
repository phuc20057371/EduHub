package com.example.eduhubvn.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pending_partner_organization")
@Data
@ToString(exclude = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingPartnerOrganization {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "business_registration_number", length = 10, nullable = false)
    private String businessRegistrationNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
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

    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    private String reason;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
