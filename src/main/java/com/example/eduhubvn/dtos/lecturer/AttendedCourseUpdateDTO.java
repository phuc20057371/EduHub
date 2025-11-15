package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendedCourseUpdateDTO {
    private LecturerDTO lecturer;
    private AttendedCourseDTO original;
    private AttendedCourseDTO update;

}
