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
    @GeneratedValue
    private Integer id;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    private String name;
    private String major;
    private String institution;
    @Column(name = "start_year")
    private Integer startYear;
    @Column(name = "graduation_year")
    private Integer graduationYear;
    private String level;
    private String url;
    private String description;

    @Column(name = "original_id")
    private Integer originalId;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    private String reason;
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;


    @ManyToOne
    @JoinColumn(name = "pending_lecturer_id")
    @JsonIgnore
    private PendingLecturer pendingLecturer;
}
