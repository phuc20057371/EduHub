package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.PendingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificationDTO {
    private  Integer id;
    private String referenceId;
    private String name;
    private String issuedBy;
    private LocalDate issueDate;
    private LocalDate  expiryDate;
    private String certificateUrl;
    private String level;
    private String description;

    private PendingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
