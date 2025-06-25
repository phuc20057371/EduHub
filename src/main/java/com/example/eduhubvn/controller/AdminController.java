package com.example.eduhubvn.controller;

import com.example.eduhubvn.dtos.admin.AllPendingApplicationsDTO;
import com.example.eduhubvn.entities.PendingEducationInstitution;
import com.example.eduhubvn.entities.PendingLecturer;
import com.example.eduhubvn.entities.PendingPartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.repositories.PendingEducationInstitutionRepository;
import com.example.eduhubvn.repositories.PendingLecturerRepository;
import com.example.eduhubvn.repositories.PendingPartnerOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/pending")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final PendingLecturerRepository pendingLecturerRepository;
    private final PendingEducationInstitutionRepository pendingEduRepo;
    private final PendingPartnerOrganizationRepository pendingPartnerRepo;

    @GetMapping("/all")
    public ResponseEntity<AllPendingApplicationsDTO> getAllPendingApplications() {
        List<PendingLecturer> lecturers = pendingLecturerRepository.findByStatus(PendingStatus.PENDING);
        List<PendingEducationInstitution> institutions = pendingEduRepo.findByStatus(PendingStatus.PENDING);
        List<PendingPartnerOrganization> partners = pendingPartnerRepo.findByStatus(PendingStatus.PENDING);

        AllPendingApplicationsDTO result = new AllPendingApplicationsDTO();
        result.setLecturers(lecturers.stream().map(LecturerMapper::toPendingLecturerResponse).toList());
        result.setEducationInstitutions(institutions.stream().map(EducationInstitutionMapper::toDTO).toList());
        result.setPartnerOrganizations(partners.stream().map(PartnerOrganizationMapper::toPendingDTO).toList());

        return ResponseEntity.ok(result);
    }
}
