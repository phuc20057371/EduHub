package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionDTO;
import com.example.eduhubvn.services.PendingEduInsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pending-edu-ins")
@RequiredArgsConstructor
public class PendingEduInsController {
    private final PendingEduInsService pendingEduInsService;

    @PostMapping("/create")
    public ResponseEntity<PendingEducationInstitutionDTO> createPenEduIns(@RequestBody PendingEducationInstitutionDTO request) {
        return ResponseEntity.ok(pendingEduInsService.createPendingEduIns(request));
    }
}
