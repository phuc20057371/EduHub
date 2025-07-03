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
public class AttendedTrainingCourseDTO {
    private Integer id;
    private String title;
    private String topic;
    private String organizer;
    private CourseType courseType;
    private Scale scale;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfHour;
    private String location;
    private String description;
    private String courseUrl;

    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
