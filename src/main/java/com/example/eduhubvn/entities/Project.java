
package com.example.eduhubvn.entities;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    private UUID id;

    private String title;
    private String domain;

    @Enumerated(EnumType.STRING)
    private ProjectCategory type; // RESEARCH, TRAINING

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    private Integer memberCount;
    private String location;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status; // DRAFT, APPROVED, IN_PROGRESS, COMPLETED...

    private Double budget;
    
    private Integer minExperienceYears;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    // Owner có thể là EducationInstitution hoặc PartnerOrganization
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private EducationInstitution educationInstitution;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private PartnerOrganization partnerOrganization;

    // Danh sách giảng viên gợi ý
    @ManyToMany
    @JoinTable(name = "project_suggested_lecturers", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "lecturer_id"))
    private List<Lecturer> suggestedLecturers;

    // Danh sách giảng viên tham gia
    @ManyToMany
    @JoinTable(name = "project_participating_lecturers", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "lecturer_id"))
    private List<Lecturer> participatingLecturers;
}
