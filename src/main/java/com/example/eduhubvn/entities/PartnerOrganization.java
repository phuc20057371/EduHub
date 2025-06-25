package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "organization_name")
    private String organizationName;
    private String industry;
    @Column(name = "tax_code")
    private String taxCode;
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

}
