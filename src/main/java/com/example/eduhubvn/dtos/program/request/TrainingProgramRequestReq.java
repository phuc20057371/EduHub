package com.example.eduhubvn.dtos.program.request;

import com.example.eduhubvn.dtos.partner.PartnerDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingProgramRequestReq {

    private PartnerDTO partner;

    private String title;
    private String description;
    private String fileUrl;

}
