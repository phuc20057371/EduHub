package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "pending_certification")
@Data
@ToString(exclude = "pendingLecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingCertification {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    private String name;
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

    @Column(name = "original_id")
    private Integer originalId;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    private String reason;
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
    @ManyToOne
    @JoinColumn(name = "pending_lecturer_id")
    @JsonIgnore
    private PendingLecturer pendingLecturer;
}
