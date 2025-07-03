package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerOrganizationReq;
import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.repositories.PartnerOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnerOrganizationService {
    private final PartnerOrganizationRepository partnerOrganizationRepository;
    private final PartnerOrganizationMapper partnerOrganizationMapper;


    public PartnerOrganizationDTO createPartnerFromUser(PartnerOrganizationReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if (user.getPartnerOrganization() != null) {
            throw new IllegalStateException("Tài khoản này đã đăng ký đơn vị đối tác.");
        }
        PartnerOrganization organization = partnerOrganizationMapper.toEntity(req);
        organization.setUser(user);
        organization.setStatus(PendingStatus.PENDING);
        PartnerOrganization saved = partnerOrganizationRepository.save(organization);

        partnerOrganizationRepository.flush();
        return partnerOrganizationMapper.toDTO(saved);
    }

    public PartnerOrganizationDTO approvePartner(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganization partnerOrganization = partnerOrganizationRepository.findById(req.getId()).orElse(null);
        if(partnerOrganization == null) {
            throw new IllegalStateException("Tài khoản này chưa đăng ký đơn vị đối tác.");
        }
        partnerOrganization.setStatus(PendingStatus.APPROVED);
        PartnerOrganization saved = partnerOrganizationRepository.save(partnerOrganization);
        return  partnerOrganizationMapper.toDTO(saved);
    }
}
