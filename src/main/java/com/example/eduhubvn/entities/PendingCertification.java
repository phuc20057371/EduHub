package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "pending_certification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingCertification {
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

    @Column(name = "original_id")
    private Integer originalId; // ID of the original certification if this is a pending request
    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    private String reason; // Reason for rejection or additional information
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt; // Time when the request was submitted
    @Column(name = "update_at")
    private LocalDateTime updatedAt; // Time when the request was last updated
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt; // Time when the request was reviewed
    @ManyToOne
    @JoinColumn(name = "pending_lecturer_id")
    @JsonIgnore
    private PendingLecturer pendingLecturer;
}
