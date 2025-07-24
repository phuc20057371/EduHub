package com.example.eduhubvn.dtos.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificationUpdateDTO {
    private LecturerDTO lecturer;
    private CertificationDTO original;
    private CertificationDTO update;
}
