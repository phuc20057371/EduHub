package com.example.eduhubvn.dtos.course;

import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.dtos.lecturer.OwnedTrainingCourseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnedCourseInfoDTO {
    private OwnedTrainingCourseDTO ownedCourse;
    private LecturerInfoDTO lecturer;
}
