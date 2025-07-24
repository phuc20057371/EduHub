package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnedCourseUpdateDTO {
    private LecturerDTO lecturer;
    private OwnedTrainingCourseDTO original;
    private OwnedTrainingCourseDTO update;
}
