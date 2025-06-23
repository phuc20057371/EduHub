package com.example.eduhubvn.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pending_lecturer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingLecturer {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "citizen_id", unique = true, nullable = false)
    private String citizenID;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
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

    @Enumerated(EnumType.STRING)
    private PendingStatus status;

    private String response; // e.g., "Approved by admin", "Rejected due to missing documents"
    @Column(name = "created_at")
    private LocalDateTime createdAt; // Thời gian tạo yêu cầu
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Thời gian cập nhật yêu cầu

    @OneToMany(mappedBy = "pendingLecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PendingCertification> pendingCertifications = new ArrayList<>();

    @OneToMany(mappedBy = "pendingLecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PendingDegree> pendingDegrees = new ArrayList<>();
}
