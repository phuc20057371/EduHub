package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certification_update")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "certification_id", nullable = false)
    private Certification certification;

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

    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    @Column(name = "admin_note")
    private String adminNote;
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
