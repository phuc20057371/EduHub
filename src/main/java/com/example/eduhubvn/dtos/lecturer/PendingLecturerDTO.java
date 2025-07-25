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
public class PendingLecturerDTO {
    private LecturerDTO lecturer;
    private List<DegreeDTO> degrees;
    private List<CertificationDTO> certifications;
}
