package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.repositories.PartnerOrganizationRepository;
import com.example.eduhubvn.repositories.PendingPartnerOrganizationRepository;
import com.example.eduhubvn.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PartnerOrganizationService {
    private final PartnerOrganizationRepository partnerOrganizationRepository;
    private final PendingPartnerOrganizationRepository pendingPartnerOrganizationRepository;
    private final UserRepository userRepository;

    @Transactional
    public PartnerOrganizationDTO approvePendingPartnerOrganization(Long pendingId) {
        PendingPartnerOrganization pending = pendingPartnerOrganizationRepository.findById(Math.toIntExact(pendingId))
                .orElseThrow(() -> new IllegalArgumentException("PendingPartnerOrganization không tồn tại với id: " + pendingId));

        if (pending.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Pending đã được duyệt trước đó.");
        }

        User user = pending.getUser();

        if (user.getPartnerOrganization() != null) {
            throw new IllegalStateException("User đã có profile PartnerOrganization.");
        }

        PartnerOrganization partner = PartnerOrganization.builder()
                .user(user)
                .organizationName(pending.getOrganizationName())
                .industry(pending.getIndustry())
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

        PartnerOrganization saved = partnerOrganizationRepository.save(partner);

        pending.setStatus(PendingStatus.APPROVED);
        pending.setUpdatedAt(LocalDateTime.now());
        pendingPartnerOrganizationRepository.save(pending);

        user.setRole(Role.ORGANIZATION);
        userRepository.save(user);

        return PartnerOrganizationMapper.toDTO(saved);
    }

}
