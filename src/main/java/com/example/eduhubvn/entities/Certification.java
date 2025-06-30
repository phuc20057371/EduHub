package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "certification")
@Data
@ToString(exclude = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    private String name;
    @Column(name = "issued_by")
    private String issuedBy;
    @Column(name = "issue_date")
    private Date issueDate;
    @Column(name = "expiry_date")
    private Date expiryDate;
    @Column(name = "certificate_url")
    private String certificateUrl;
    private String level;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;



}
