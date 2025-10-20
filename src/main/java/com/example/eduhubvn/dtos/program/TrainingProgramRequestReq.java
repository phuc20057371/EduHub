package com.example.eduhubvn.dtos.program;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramRequestReq {

    private PartnerOrganizationDTO partner;

    private String title;
    private String description;
    private String fileUrl;

}
