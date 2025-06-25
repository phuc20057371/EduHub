package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@ToString(exclude = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "education_institution")
public class EducationInstitution {
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "institution_name")
    private String institutionName;
    @Column(name = "institution_type")
    @Enumerated(EnumType.STRING)
    private EducationInstitutionType institutionType;
    @Column(name = "tax_code")
    private String taxCode;
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



}
