package com.example.eduhubvn.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "degree")
@Data
@ToString(exclude = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Degree {
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
}
