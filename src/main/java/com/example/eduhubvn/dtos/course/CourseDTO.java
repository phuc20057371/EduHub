package com.example.eduhubvn.dtos.course;

import com.example.eduhubvn.entities.CourseType;
import com.example.eduhubvn.entities.Scale;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Integer id;
    private String title;
    private String topic;
    @Enumerated(EnumType.STRING)
    private CourseType courseType;
    @Enumerated(EnumType.STRING)
    private Scale scale;
    private String description;
    private String thumbnailUrl;
    private String contentUrl;
    private String level;
    private String requirements;
    private String language;
    private Boolean isOnline;
    private String address;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    private Double price;
    @Column(name = "is_published")
    private Boolean isPublished;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
