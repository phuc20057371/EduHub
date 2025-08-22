package com.example.eduhubvn.dtos.lecturer;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LecturerAllProfileDTO {
    private LecturerInfoDTO lecturer;
    private LecturerUpdateDTO lecturerUpdate;
    private List<DegreeUpdateDTO> degrees;
    private List<CertificationUpdateDTO> certifications;
    private List<OwnedCourseUpdateDTO> ownedCourses;
    private List<AttendedCourseUpdateDTO> attendedCourses;
    private List<ResearchProjectUpdateDTO> researchProjects;

}
