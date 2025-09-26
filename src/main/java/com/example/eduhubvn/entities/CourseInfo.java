package com.example.eduhubvn.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo {
    @Id
    private UUID id; // dùng chung id với Project (khóa chính = project_id)

    @Column(name = "public_title")
    private String publicTitle;
    @Column(name = "public_description", columnDefinition = "TEXT")
    private String publicDescription;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "knowledges", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "knowledge")
    private List<String> knowledge = new ArrayList<>();

    @Column(columnDefinition = "TEXT", name = "introduce")
    private String introduce;
    private BigDecimal price;
    private boolean published; // Đã xuất bản hay chưa

    // system fields
    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "project_id")
    private Project project;

}
