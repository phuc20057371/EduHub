package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "certification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String issuedBy;
    @Column(name = "issue_date")
    private Date issueDate;
    @Column(name = "expiry_date")
    private Date expiryDate;
    @Column(name = "certificate_url")
    private String certificateUrl;
    private String level; // e.g., Beginner, Intermediate, Advanced
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;



}
