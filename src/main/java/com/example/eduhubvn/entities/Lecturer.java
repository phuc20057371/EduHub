package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lecturer")
@Data
@ToString(exclude = { "user", "certifications", "degrees",
        "researchProjects", "attendedTrainingCourses", "ownedTrainingCourses"
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("hidden = false")
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "citizen_id", length = 11, nullable = false, unique = true)
    private String citizenId;

    @OneToOne(cascade = { CascadeType.REFRESH, CascadeType.DETACH }, orphanRemoval = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "gender", nullable = false)
    private Boolean gender;
    @Column(name = "bio")
    private String bio;
    @Column(name = "address")
    private String address;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "academic_rank")
    @Enumerated(EnumType.STRING)
    private AcademicRank academicRank;
    @Column(name = "specialization")
    private String specialization;
    @Column(name = "experience_years")
    private Integer experienceYears;
    @Column(name = "job_field")
    private String jobField;

    private boolean hidden;

    private String adminNote;
    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Degree> degrees;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResearchProject> researchProjects;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendedTrainingCourse> attendedTrainingCourses;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnedTrainingCourse> ownedTrainingCourses;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseLecturer> courseLecturers;

    @OneToOne(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private LecturerUpdate lecturerUpdate;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

}
