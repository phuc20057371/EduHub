package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerProfileDTO {
    private LecturerInfoDTO lecturer;
    private LecturerDTO lecturerUpdate;
    private List<DegreeDTO> degrees;
    private List<CertificationDTO> certificates;
    private List<OwnedTrainingCourseDTO> ownedTrainingCourses;
    private List<AttendedTrainingCourseDTO> attendedTrainingCourses;
    private List<ResearchProjectDTO> researchProjects;

}
