package com.example.eduhubvn.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "owned_training_course")
@Data
@ToString(exclude = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnedTrainingCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String topic;
    @Enumerated(EnumType.STRING)
    @Column(name="course_type")
    private CourseType courseType;

    @Enumerated(EnumType.STRING)
    private Scale scale;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    @Column(name = "content_url")
    private String contentUrl;
    private String level; // e.g., Beginner, Intermediate, Advanced
    private String requirements; // Prerequisites for the course
    private String language;
    @Column(name = "is_online")
    private Boolean isOnline; // Whether the course is online or in-person
    private String address;
    private Double price;

    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    private String description;
    @Column(name = "course_url")
    private String courseUrl;

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

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;


}
