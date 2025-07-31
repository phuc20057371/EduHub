package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "course_lecturer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseLecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
    @Enumerated(EnumType.STRING)
    private CourseRole role;
}
