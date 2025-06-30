package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerCourseDTO {
    private Integer lecturerId;
    private String citizenId;
    private String fullName;
    private Date dateOfBirth;
    private Boolean gender;
    private String bio;
    private String address;
    private String avatarUrl;
    private String academicRank;
    private String specialization;
    private Integer experienceYears;

    private List<OwnedTrainingCourseDTO> ownedCourses;
    private List<AttendedTrainingCourseDTO> attendedCourses;
    private List<ResearchProjectDTO> researchProjects;

    private List<PendingOwnedTrainingCourseDTO> pendingOwnedCourses;
    private List<PendingAttendedTrainingCourseDTO> pendingAttendedCourses;
    private List<PendingResearchProjectDTO> pendingResearchProjects;
}
