package com.example.eduhubvn.dtos.project.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseModuleGuestDTO {
    private UUID id;
    private String title;
    private String description;
    private Integer duration;
    private Integer moduleOrder;

    private LecturerInfoDTO lecturer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
