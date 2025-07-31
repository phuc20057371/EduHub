package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "research_project")
@Data
@ToString(exclude = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchProject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    @Column(name = "research_area")
    private String researchArea;
    @Enumerated(EnumType.STRING)
    private Scale scale;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "founding_amount")
    private Double foundingAmount;
    @Column(name = "founding_source")
    private String foundingSource;
    @Enumerated(EnumType.STRING)
    @Column(name = "project_type")
    private ProjectType projectType;
    @Column(name = "role_in_project")
    private String roleInProject;
    @Column(name = "published_url")
    private String publishedUrl;
    @Column(name = "course_status")
    private String courseStatus;
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

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}
