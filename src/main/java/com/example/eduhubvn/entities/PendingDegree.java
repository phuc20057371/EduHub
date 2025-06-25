package com.example.eduhubvn.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pending_degree")
@Data
@ToString(exclude = "pendingLecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingDegree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name; // e.g., Bachelor's, Master's, PhD
    private String major; // e.g., Computer Science, Mathematics
    private String institution; // e.g., University of XYZ
    @Column(name = "start_year")
    private Integer startYear;
    @Column(name = "graduation_year")
    private Integer graduationYear; // e.g., 2020, 2021
    private String level;
    private String url;
    @Column(name = "reference_id")
    private String referenceID;
    @Column(name = "req_no")
    private String reqNo ;
    private String description;

    @Column(name = "original_id")
    private Integer originalId; // ID of the original degree if this is a pending request
    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    private String reason; // Reason for rejection or additional information
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt; // Time when the request was submitted
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Time when the request was last updated
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt; // Time when the request was reviewed


    @ManyToOne
    @JoinColumn(name = "pending_lecturer_id")
    @JsonIgnore
    private PendingLecturer pendingLecturer;
}
