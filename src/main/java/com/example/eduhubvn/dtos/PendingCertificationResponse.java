package com.example.eduhubvn.dtos;

import com.example.eduhubvn.entities.PendingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingCertificationResponse {
    private String name;
    private String issuedBy;
    private Date issueDate;
    private Date expiryDate;
    private String certificateUrl;
    private String level;
    private String description;
}
