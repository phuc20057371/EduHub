package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.eduhubvn.enums.PendingStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "degree")
@Data
@ToString(exclude = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @OneToOne(mappedBy = "degree", cascade = CascadeType.ALL, orphanRemoval = true)
    private DegreeUpdate degreeUpdate;
}
