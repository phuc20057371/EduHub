package com.example.eduhubvn.dtos.project.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.eduhubvn.enums.CourseLevel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseGuestDTO {
    private UUID id;
    private String publicTitle;
    private String thumbnailUrl;
    private String introduce;
    private String publicDescription;

    private CourseLevel level;
    private List<String> knowledge;
    private List<String> requirements;

    private boolean published;

    private Boolean isOnline;
    private String address;

    private BigDecimal price;

    private List<CourseModuleGuestDTO> courseModules;
}
