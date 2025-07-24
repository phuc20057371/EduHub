package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResearchProjectUpdateDTO {
    private LecturerDTO lecturer;
    private ResearchProjectDTO original;
    private ResearchProjectDTO update;

}
