package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LecturerAllInfoDTO {
    private LecturerInfoDTO lecturer;
    private List<DegreeDTO> degrees;
    private List<CertificationDTO> certifications;
    private List<OwnedTrainingCourseDTO> ownedTrainingCourses;
    private List<AttendedTrainingCourseDTO> attendedTrainingCourses;
    private List<ResearchProjectDTO> researchProjects;
}
