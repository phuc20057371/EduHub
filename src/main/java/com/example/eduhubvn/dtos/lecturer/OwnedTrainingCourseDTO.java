package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.enums.CourseType;
import com.example.eduhubvn.enums.PendingStatus;
import com.example.eduhubvn.enums.Scale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnedTrainingCourseDTO {
    private UUID id;
    private String title;
    private String topic;
    private CourseType courseType;
    private Scale scale;
    private String thumbnailUrl;
    private String contentUrl;
    private String level; 
    private String requirements; 
    private String language;
    private Boolean isOnline;
    private String address;
    private Double price;

    private LocalDate startDate;
    private LocalDate endDate;

    private String description;
    private String courseUrl;

    private PendingStatus status;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
