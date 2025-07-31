package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "degree_update")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DegreeUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "degree_id", nullable = false)
    private Degree degree;

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

    @Column(name = "admin_note")
    private String adminNote;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

}
