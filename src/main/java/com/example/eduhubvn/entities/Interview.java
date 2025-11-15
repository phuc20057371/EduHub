package com.example.eduhubvn.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.eduhubvn.enums.InterviewMode;
import com.example.eduhubvn.enums.InterviewResult;
import com.example.eduhubvn.enums.InterviewStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interview")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "interview_date", nullable = false)
    private LocalDateTime interviewDate;
    @Column(name = "location", nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false)
    private InterviewMode mode; // ONLINE, OFFLINE

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InterviewStatus status; // SCHEDULED, COMPLETED, CANCELED

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "score")
    private Integer score; // Điểm phỏng vấn (0-100)

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private InterviewResult result; // PASS, FAIL, PENDING

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

}
