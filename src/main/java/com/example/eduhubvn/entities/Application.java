package com.example.eduhubvn.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.eduhubvn.enums.ApplicationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String cvUrl;

    @Column(columnDefinition = "TEXT", name = "cover_letter")
    private String coverLetter;

    private BigDecimal expectedSalary;

    private ApplicationStatus status; // SUBMITTED, REVIEW, INTERVIEW, APPROVED, REJECTED

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    /// Relationships
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY)
    private List<Interview> interviews;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Contract contract;

    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY)
    private List<ApplicationModule> applicationModules;


}
