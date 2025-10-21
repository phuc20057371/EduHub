package com.example.eduhubvn.dtos.program;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramUnitPublicDTO {
    private UUID id;
    private LecturerUnitPublicDTO lecturer;
    private String title;
    private String description;
    private Integer durationSection;
    private Integer orderSection;
    private boolean lead;
}
