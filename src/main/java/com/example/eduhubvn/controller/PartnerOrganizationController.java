package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.services.PartnerOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/p-org")
@RequiredArgsConstructor
public class PartnerOrganizationController {
    private final PartnerOrganizationService partnerOrganizationService;

    @PostMapping("/from-pending/{id}")
    public ResponseEntity<PartnerOrganizationDTO> approvePendingOrganization(@PathVariable Integer id) {
        PartnerOrganizationDTO approved = partnerOrganizationService.approvePendingPartnerOrganization(Long.valueOf(id));
        return ResponseEntity.ok(approved);
    }
}
