package com.example.eduhubvn.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "lecturer")
@Data
@EqualsAndHashCode(exclude = { "lecturerUpdate" })
@ToString(exclude = { "user", "certifications", "degrees",
        "researchProjects", "attendedTrainingCourses", "ownedTrainingCourses"
})

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "lecturer_id", unique = true)
    private String lecturerId;

    @Column(name = "citizen_id", length = 12, nullable = false, unique = true)
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
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    @Column(name = "address", columnDefinition = "TEXT")
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
    @Column(name = "job_field" , columnDefinition = "TEXT")
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

    @OneToOne(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private LecturerUpdate lecturerUpdate;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

}
