package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String topic;
    @Enumerated(EnumType.STRING)
    @Column(name = "course_type")
    private CourseType courseType;
    private String description;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    @Column(name = "content_url")
    private String contentUrl; // URL to the course content, e.g., video or document
    private String level; // e.g., Beginner, Intermediate, Advanced
    private String requirements; // Prerequisites for the course
    private String language;
    @Column(name = "is_online")
    private Boolean isOnline; // Whether the course is online or in-person
    private String address;

    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    private Double price;
    @Column(name = "is_published")
    private Boolean isPublished;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseLecturer> courseLecturers;

    @OneToOne
    @JoinColumn(name = "ownedCourse_id")
    private OwnedTrainingCourse ownedTrainingCourse;


}
