package com.example.eduhubvn.dtos.lecturer;

import com.example.eduhubvn.entities.PendingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingCertificationDTO {
    private Integer id;
    private String name;
    private String issuedBy;
    private Date issueDate;
    private Date expiryDate;
    private String certificateUrl;
    private String level;
    private String description;

    private Integer originalId;
    private PendingStatus status;
    private String reason;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reviewedAt;
}
