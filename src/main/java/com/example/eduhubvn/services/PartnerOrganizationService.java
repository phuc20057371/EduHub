package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerOrganizationReq;
import com.example.eduhubvn.dtos.partner.request.PartnerUpdateReq;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.mapper.PartnerOrganizationUpdateMapper;
import com.example.eduhubvn.repositories.PartnerOrganizationRepository;
import com.example.eduhubvn.repositories.PartnerOrganizationUpdateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerOrganizationService {

    private final PartnerOrganizationRepository partnerOrganizationRepository;
    private final PartnerOrganizationUpdateRepository partnerOrganizationUpdateRepository;

    private final PartnerOrganizationMapper partnerOrganizationMapper;
    private final PartnerOrganizationUpdateMapper partnerOrganizationUpdateMapper;


    public PartnerOrganizationDTO createPartnerFromUser(PartnerOrganizationReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if (user.getPartnerOrganization() != null) {
            throw new IllegalStateException("Tài khoản này đã đăng ký đơn vị đối tác.");
        }
        PartnerOrganization saved = null;
        try {
            PartnerOrganization organization = partnerOrganizationMapper.toEntity(req);
            organization.setUser(user);
            organization.setStatus(PendingStatus.PENDING);
            saved = partnerOrganizationRepository.save(organization);

            partnerOrganizationRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        PartnerOrganization saved = null;
        try {
            partnerOrganization.getUser().setRole(Role.ORGANIZATION);
            partnerOrganization.setStatus(PendingStatus.APPROVED);
            saved = partnerOrganizationRepository.save(partnerOrganization);
            partnerOrganizationRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  partnerOrganizationMapper.toDTO(saved);
    }

    @Transactional
    public PartnerUpdateReq updatePartnerFromUser(PartnerUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganization partnerOrganization = user.getPartnerOrganization();
        if (partnerOrganization == null) {
            throw new IllegalStateException("Người dùng chưa đăng ký tổ chức đối tác.");
        }
        try {
            Optional<PartnerOrganizationUpdate> optionalUpdate =
                    partnerOrganizationUpdateRepository.findByPartnerOrganization(partnerOrganization);
            PartnerOrganizationUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                partnerOrganizationUpdateMapper.updateEntityFromDto(req, update);
            } else {
                update = partnerOrganizationUpdateMapper.toUpdate(req);
                update.setPartnerOrganization(partnerOrganization);
            }
            update.setStatus(PendingStatus.PENDING);
            partnerOrganizationUpdateRepository.save(update);
            partnerOrganizationUpdateRepository.flush();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý cập nhật đối tác: " + e.getMessage(), e);
        }

        return req;
    }


    @Transactional
    public List<PartnerOrganizationPendingDTO> getPendingPartnerOrganizationUpdates() {
        List<PartnerOrganizationUpdate> pendingUpdates =
                partnerOrganizationUpdateRepository.findByStatus(PendingStatus.PENDING);

        return pendingUpdates.stream()
                .map(update -> {
                    PartnerOrganization organization = update.getPartnerOrganization();
                    PartnerOrganizationDTO orgDTO = partnerOrganizationMapper.toDTO(organization);
                    PartnerOrganizationUpdateDTO updateDTO = partnerOrganizationUpdateMapper.toDTO(update);
                    return PartnerOrganizationPendingDTO.builder()
                            .partnerOrganization(orgDTO)
                            .partnerOrganizationUpdate(updateDTO)
                            .build();
                })
                .collect(Collectors.toList());
    }

}
