package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.services.PartnerOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {




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
