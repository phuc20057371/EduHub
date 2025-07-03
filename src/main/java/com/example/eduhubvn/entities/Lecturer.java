package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lecturer")
@Data
@ToString(exclude = {"user", "certifications", "degrees",
        "researchProjects" ,"attendedTrainingCourses", "ownedTrainingCourses"
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecturer {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "citizen_id", length = 11, nullable = false, unique = true)
    private String citizenId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column (name ="gender" , nullable = false)
    private Boolean gender;
    @Column(name = "bio")
    private String bio;
    @Column(name = "address")
    private String address;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column (name = "academic_rank")
    private String academicRank;
    @Column(name = "specialization")
    private String specialization;
    @Column(name = "experience_years")
    private Integer experienceYears;

    private PendingStatus status;
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications = new ArrayList<>();

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Degree> degrees = new ArrayList<>();

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResearchProject> researchProjects = new ArrayList<>();

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendedTrainingCourse> attendedTrainingCourses = new ArrayList<>();

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnedTrainingCourse> ownedTrainingCourses = new ArrayList<>();


}
