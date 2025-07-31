package com.example.eduhubvn.dtos.lecturer.request;

import com.example.eduhubvn.entities.CourseType;
import com.example.eduhubvn.entities.Scale;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendedTrainingCourseUpdateReq {
    private UUID id;
    private String title;
    private String topic;
    private String organizer;
    @Enumerated(EnumType.STRING)
    private CourseType courseType;
    @Enumerated(EnumType.STRING)
    private Scale scale;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfHour;
    private String location;
    private String description;
    private String courseUrl;
}
