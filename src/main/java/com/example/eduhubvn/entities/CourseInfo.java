package com.example.eduhubvn.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.eduhubvn.enums.CourseLevel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_infos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "public_title", length = 500)
    private String publicTitle;
    @Column(name = "public_description", columnDefinition = "TEXT")
    private String publicDescription;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    private CourseLevel level;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "knowledges", joinColumns = @JoinColumn(name = "course_info_id"))
    @Column(name = "knowledge")
    private List<String> knowledge = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "requirements", joinColumns = @JoinColumn(name = "course_info_id"))
    @Column(name = "requirement")
    private List<String> requirements = new ArrayList<>();

    @Column(columnDefinition = "TEXT", name = "introduce")
    private String introduce;
    private BigDecimal price;
    private boolean published; // Đã xuất bản hay chưa

    private String address;
    @Column(name = "is_online")
    private boolean isOnline;

    // system fields
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @OneToOne(cascade = { CascadeType.REFRESH, CascadeType.DETACH })
    @JoinColumn(name = "project_id")
    private Project project;

}
