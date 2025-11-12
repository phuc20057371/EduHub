package com.example.eduhubvn.dtos.program;

import java.util.UUID;

import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingUnitDTO {

    private UUID id;
    private LecturerInfoDTO lecturer;
    private String title;
    private String description;
    private Integer durationSection;
    private Integer orderSection;
    private boolean lead;
    private String trialVideoUrl;

}
