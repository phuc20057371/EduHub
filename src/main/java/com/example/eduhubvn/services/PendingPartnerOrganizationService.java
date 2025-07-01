package com.example.eduhubvn.services;


import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PendingPartnerOrganizationDTO;
import com.example.eduhubvn.entities.PendingPartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.repositories.PendingPartnerOrganizationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PendingPartnerOrganizationService {
    private final PendingPartnerOrganizationRepository pendingRepo;

    @Transactional
    public PendingPartnerOrganizationDTO createPendingOrganization(PartnerOrganizationDTO req) {
        if (req == null) {
            throw new IllegalArgumentException("Thông tin tổ chức không được để trống.");
        }

        if (req.getBusinessRegistrationNumber() == null || req.getBusinessRegistrationNumber().isBlank()) {
            throw new IllegalArgumentException("Mã số đăng ký kinh doanh không được để trống.");
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy người dùng đang đăng nhập.");
        }

        if (user.getPendingPartnerOrganization() != null) {
            throw new IllegalStateException("Bạn đã có hồ sơ tổ chức đang chờ duyệt.");
        }

        PendingPartnerOrganization pending = PendingPartnerOrganization.builder()
                .businessRegistrationNumber(req.getBusinessRegistrationNumber())
                .organizationName(req.getOrganizationName())
                .industry(req.getIndustry())
                .phoneNumber(req.getPhoneNumber())
                .website(req.getWebsite())
                .address(req.getAddress())
                .representativeName(req.getRepresentativeName())
                .position(req.getPosition())
                .description(req.getDescription())
                .establishedYear(req.getEstablishedYear())
                .logoUrl(req.getLogoUrl() != null ? req.getLogoUrl() : "https://picsum.photos/200")
                .status(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        user.setPendingPartnerOrganization(pending);
        PendingPartnerOrganization saved = pendingRepo.save(pending);

        return PartnerOrganizationMapper.toPendingDTO(saved);
    }

}
