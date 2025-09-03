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
    private List<DegreeUpdateDTO> degrees;
    private List<CertificationUpdateDTO> certificates;
    private List<OwnedCourseUpdateDTO> ownedCourses;
    private List<AttendedCourseUpdateDTO> attendedCourses;
    private List<ResearchProjectUpdateDTO> researchProjects;

}
