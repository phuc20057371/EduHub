package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attended_training_course")
@Data
@ToString(exclude = "lecturer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendedTrainingCourse {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;
    private String topic;
    private String organizer;
    @Enumerated(EnumType.STRING)
    @Column(name="course_type")
    private CourseType courseType;
    @Enumerated(EnumType.STRING)
    private Scale scale;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "number_of_hour")
    private Integer numberOfHour;
    private String location;
    private String description;
    @Column(name = "course_url")
    private String courseUrl;

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
