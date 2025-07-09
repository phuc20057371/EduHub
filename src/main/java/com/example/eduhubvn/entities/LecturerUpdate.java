package com.example.eduhubvn.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lecturer_update")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerUpdate {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column (name ="gender")
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
}
