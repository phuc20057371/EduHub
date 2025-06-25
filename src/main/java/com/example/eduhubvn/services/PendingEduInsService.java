package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionReq;
import com.example.eduhubvn.entities.PendingEducationInstitution;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.repositories.PendingEducationInstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PendingEduInsService {
    private final PendingEducationInstitutionRepository pendingEducationInstitutionRepository;

    public PendingEducationInstitutionDTO createPendingEduIns(PendingEducationInstitutionReq request){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }

        if (user.getPendingEducationInstitution() != null) {
            throw new IllegalStateException("Profile already exists for this user.");
        }
        PendingEducationInstitution pendingEducationInstitution = PendingEducationInstitution.builder()
                .user(user)
                .institutionName(request.getInstitutionName())
                .institutionType(request.getInstitutionType())
                .taxCode(request.getTaxCode())
                .phoneNumber(request.getPhoneNumber())
                .website(request.getWebsite())
                .address(request.getAddress())
                .representativeName(request.getRepresentativeName())
                .position(request.getPosition())
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .establishedYear(request.getEstablishedYear())
                .status(PendingStatus.PENDING)
                .reason("")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        pendingEducationInstitutionRepository.save(pendingEducationInstitution);
        return EducationInstitutionMapper.toDTO(pendingEducationInstitution);
    }
}
