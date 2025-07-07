package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.partner.request.PartnerUpdateReq;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.services.PartnerOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
public class OrganizationController {

    private final PartnerOrganizationService partnerOrganizationService;
    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse<PartnerUpdateReq>> updatePartnerFromUser(@RequestBody PartnerUpdateReq req, @AuthenticationPrincipal User user) {
        PartnerUpdateReq request = partnerOrganizationService.updatePartnerFromUser(req, user);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi yêu cầu cập nhật", request));
    }


    @GetMapping
    public String get(){
        return "GET:: ORGANIZATION";
    }
    @PostMapping
    public String post(){
        return "POST:: ORGANIZATION";
    }
    @PutMapping
    public String put(){
        return "PUT:: ORGANIZATION";
    }
    @DeleteMapping
    public String delete(){
        return "DELETE:: ORGANIZATION";
    }


}
