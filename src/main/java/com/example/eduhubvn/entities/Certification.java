package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


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
    private LocalDate issueDate;
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    @Column(name = "certificate_url")
    private String certificateUrl;
    private String level;
    @Column(name = "description")
    private String description;

    private PendingStatus status;
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;



}
