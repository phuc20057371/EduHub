package com.example.eduhubvn.services;


import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import com.example.eduhubvn.entities.PendingPartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.repositories.PendingPartnerOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PendingPartnerOrganizationService {
    private final PendingPartnerOrganizationRepository pendingRepo;

    public PendingPartnerOrganizationDTO createPendingOrganization(PartnerOrganizationDTO req) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        if (user.getPendingPartnerOrganization() != null) {
            throw new IllegalStateException("PendingPartnerOrganization profile already exists for this user.");
        }

        PendingPartnerOrganization pending = PendingPartnerOrganization.builder()
                .businessRegistrationNumber(req.getBusinessRegistrationNumber())
                .user(user)
                .organizationName(req.getOrganizationName())
                .industry(req.getIndustry())
                .phoneNumber(req.getPhoneNumber())
                .website(req.getWebsite())
                .address(req.getAddress())
                .representativeName(req.getRepresentativeName())
                .position(req.getPosition())
                .description(req.getDescription())
                .logoUrl("https://picsum.photos/200")
                .establishedYear(req.getEstablishedYear())
                .status(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        user.setPendingPartnerOrganization(pending);
        pendingRepo.save(pending);

        return PartnerOrganizationMapper.toPendingDTO(pending);
    }
}
