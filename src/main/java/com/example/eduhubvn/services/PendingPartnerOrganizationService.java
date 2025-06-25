package com.example.eduhubvn.services;


import com.example.eduhubvn.dtos.partner.PartnerOrganizationReq;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import com.example.eduhubvn.entities.PendingPartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.repositories.PendingPartnerOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PendingPartnerOrganizationService {
    private final PendingPartnerOrganizationRepository pendingRepo;

    public PendingPartnerOrganizationDTO register(PartnerOrganizationReq req) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }
        if (user.getPendingPartnerOrganization() != null) {
            throw new IllegalStateException("PendingPartnerOrganization profile already exists for this user.");
        }

        PendingPartnerOrganization pending = PendingPartnerOrganization.builder()
                .user(user)
                .organizationName(req.getOrganizationName())
                .industry(req.getIndustry())
                .taxCode(req.getTaxCode())
                .phoneNumber(req.getPhoneNumber())
                .website(req.getWebsite())
                .address(req.getAddress())
                .representativeName(req.getRepresentativeName())
                .position(req.getPosition())
                .description(req.getDescription())
                .logoUrl(req.getLogoUrl())
                .establishedYear(req.getEstablishedYear())
                .status(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

// ✅ Gán ngược lại quan hệ
        user.setPendingPartnerOrganization(pending);

// ✅ Lưu lại pending (nếu cascade đúng, user sẽ tự cập nhật)
        pendingRepo.save(pending);

        return PendingPartnerOrganizationDTO.builder()
                .id(pending.getId())
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
                .status(pending.getStatus().name())
                .reason(pending.getReason())
                .createdAt(pending.getCreatedAt())
                .updatedAt(pending.getUpdatedAt())
                .build();
    }
}
