package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.CourseType;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.Scale;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class AttendedCourseDTO {
    private UUID id;
    private String title;
    private String topic;
    private String organizer;
    @Enumerated(EnumType.STRING)
    private CourseType courseType;
    private Scale scale;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfHour;
    private String location;
    private String description;
    private String courseUrl;

    @Enumerated(EnumType.STRING)
    private PendingStatus status;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
