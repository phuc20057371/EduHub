package com.example.eduhubvn.dtos.course;

import com.example.eduhubvn.entities.CourseType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseReq {

    private String title;
    private String topic;
    private CourseType courseType;
    private String description;
    private String thumbnailUrl;
    private String contentUrl;
    private String level;
    private String requirements;
    private String language;
    private Boolean isOnline;
    private String address;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Boolean isPublished;

    private UUID ownedCourseId;
    private UUID authorId;
}
