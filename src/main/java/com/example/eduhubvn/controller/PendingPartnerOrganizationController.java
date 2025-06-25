package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationReq;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import com.example.eduhubvn.services.PendingPartnerOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/p-partner-org")
@RequiredArgsConstructor
public class PendingPartnerOrganizationController {
    private final PendingPartnerOrganizationService partnerOrganizationService;

    @PostMapping("/create")
    public ResponseEntity<PendingPartnerOrganizationDTO> registerPartnerOrg(@RequestBody PartnerOrganizationReq req) {
        PendingPartnerOrganizationDTO created = partnerOrganizationService.register(req);
        return ResponseEntity.ok(created);
    }
}
