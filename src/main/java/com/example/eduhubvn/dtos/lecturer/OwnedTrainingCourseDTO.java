package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.CourseType;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.Scale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnedTrainingCourseDTO {
    private Integer id;
    private String title;
    private String topic;
    private CourseType courseType;
    private Scale scale;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfHour;
    private String location;
    private String courseStatus;
    private String description;
    private String courseUrl;

    private PendingStatus status;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
