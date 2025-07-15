package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DegreePendingDTO {
    private DegreeDTO degree;
    private DegreeDTO updatedDegree;
    private LecturerDTO lecturer;
}
