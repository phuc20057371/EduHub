package com.example.eduhubvn.dtos.lecturer.request;

import com.example.eduhubvn.entities.CourseType;
import com.example.eduhubvn.entities.Scale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnedTrainingCourseUpdateReq {
    private UUID id;
    private String title;
    private String topic;
    private CourseType courseType;
    private Scale scale;
    private String thumbnailUrl;
    private String contentUrl;
    private String level; // e.g., Beginner, Intermediate, Advanced
    private String requirements; // Prerequisites for the course
    private String language;
    private Boolean isOnline; // Whether the course is online or in-person
    private String address;
    private Double price;

    private LocalDate startDate;
    private LocalDate endDate;

    private String description;
    private String courseUrl;
}
