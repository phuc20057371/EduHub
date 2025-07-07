package com.example.eduhubvn.dtos.lecturer.request;

import com.example.eduhubvn.entities.CourseType;
import com.example.eduhubvn.entities.Scale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnedTrainingCourseReq {
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
}
