package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "pending_research_project")
@Data
@ToString(exclude = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingResearchProject {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    @Column(name = "research_area")
    private String researchArea;
    @Enumerated(EnumType.STRING)
    private Scale scale;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
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
    private String status;
    private String description;

    private Integer originalId;

    @Enumerated(EnumType.STRING)
    private PendingStatus pendingStatus;
    private String reason;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}

