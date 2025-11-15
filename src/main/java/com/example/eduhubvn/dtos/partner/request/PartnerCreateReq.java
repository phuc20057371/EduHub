package com.example.eduhubvn.dtos.partner.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerCreateReq {
    private String businessRegistrationNumber;
    private String organizationName;
    private String industry;
    private String phoneNumber;
    private String website;
    private String address;
    private String representativeName;
    private String position;
    private String description;
    private String logoUrl;
    private Integer establishedYear;
}
