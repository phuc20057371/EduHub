package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.services.EducationInstitutionService;
import com.example.eduhubvn.services.PendingEduInsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/edu")
@RequiredArgsConstructor
public class EduInsController {
    private final EducationInstitutionService educationInstitutionService;


    @PostMapping("/approve/{pendingId}")
    public ResponseEntity<EducationInstitutionDTO> approvePendingEduInstitution(@PathVariable Integer pendingId) {
        EducationInstitutionDTO approved = educationInstitutionService.approvePendingInstitution(pendingId);

        return ResponseEntity.ok(approved);
    }
}
