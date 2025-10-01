
package com.example.eduhubvn.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /// HR fields
    @Column(length = 500)
    private String title;
    @Enumerated(EnumType.STRING)
    private ProjectCategory type; // RESEARCH, COURSE
    @Column(length = 255)
    private String field; // Công nghệ thông tin, Kinh tế, Y dược...
    @Column(length = 1000)
    private String description; // Mô tả ngắn gọn

    // secret fields
    @Column(name = "member_count")
    private Integer memberCount; // Số lượng thành viên cần tuyển
    private BigDecimal budget; // Ngân sách dự kiến

    @Enumerated(EnumType.STRING)
    private ProjectStatus status; // PREPARE, REVIEW, PROCESS, SUCCESS, COMPLETED

    @Column(columnDefinition = "TEXT", name = "job_description")
    private String jobDescription;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "project_requirements", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "requirement")
    private List<String> requirements = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "project_benefits", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "benefit")
    private List<String> benefits = new ArrayList<>();

    @Column(nullable = false)
    private boolean published;

    // public fields
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    private Integer duration; // Số lượng
    @Column(name = "duration_unit")
    private String durationUnit; // DAYS, WEEKS, MONTHS ...

    @Column(name = "is_remote")
    private boolean isRemote; // Có thể làm việc từ xa không
    @Column(length = 500)
    private String location; // Địa điểm thực hiện (Có thể là online)

    // system fields
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    // Relationships

    // Owner có thể là EducationInstitution hoặc PartnerOrganization
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private EducationInstitution educationInstitution;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private PartnerOrganization partnerOrganization;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CourseInfo courseInfo;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CourseModule> courseModules;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Application> applications;

}
