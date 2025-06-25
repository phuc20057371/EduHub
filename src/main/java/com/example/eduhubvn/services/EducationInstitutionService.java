package com.example.eduhubvn.services;


import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.repositories.EducationInstitutionRepository;
import com.example.eduhubvn.repositories.PendingEducationInstitutionRepository;
import com.example.eduhubvn.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EducationInstitutionService {
    private final EducationInstitutionRepository educationInstitutionRepository;
    private final PendingEducationInstitutionRepository pendingEducationInstitutionRepository;
    private final UserRepository userRepository;

    @Transactional
    public EducationInstitutionDTO approvePendingInstitution(Integer pendingId) {
        PendingEducationInstitution pending = pendingEducationInstitutionRepository.findById(pendingId)
                .orElseThrow(() -> new IllegalArgumentException("PendingEducationInstitution không tồn tại với id: " + pendingId));

        if (pending.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Pending đã được duyệt trước đó.");
        }

        User user = pending.getUser();

        if (user.getEducationInstitution() != null) {
            throw new IllegalStateException("User đã có profile EducationInstitution.");
        }

        EducationInstitution edu = EducationInstitution.builder()
                .user(user)
                .institutionName(pending.getInstitutionName())
                .institutionType(pending.getInstitutionType())
                .taxCode(pending.getTaxCode())
                .phoneNumber(pending.getPhoneNumber())
                .website(pending.getWebsite())
                .address(pending.getAddress())
                .representativeName(pending.getRepresentativeName())
                .position(pending.getPosition())
                .description(pending.getDescription())
                .logoUrl(pending.getLogoUrl())
                .establishedYear(pending.getEstablishedYear())
                .build();

        // Save new EducationInstitution
        EducationInstitution saved = educationInstitutionRepository.save(edu);

        // Update PendingEducationInstitution status
        pending.setStatus(PendingStatus.APPROVED);
        pending.setUpdatedAt(LocalDateTime.now());
        pendingEducationInstitutionRepository.save(pending);

        user.setRole(Role.SCHOOL);
        userRepository.save(user);

        return EducationInstitutionMapper.toDTO(edu);
    }

}
