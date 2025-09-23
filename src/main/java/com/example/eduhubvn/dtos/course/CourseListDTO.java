package com.example.eduhubvn.dtos.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseListDTO {
    private List<CourseInfoDTO> courses;
    private int total;
}