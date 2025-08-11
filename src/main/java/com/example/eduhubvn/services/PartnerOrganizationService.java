package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerOrganizationReq;
import com.example.eduhubvn.dtos.partner.request.PartnerUpdateReq;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.PartnerOrganizationMapper;
import com.example.eduhubvn.repositories.PartnerOrganizationRepository;
import com.example.eduhubvn.repositories.PartnerOrganizationUpdateRepository;
import jakarta.persistence.EntityNotFoundException;
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


    @Transactional
    public PartnerOrganizationDTO createPartnerFromUser(PartnerOrganizationReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if( partnerOrganizationRepository.existsByBusinessRegistrationNumber(req.getBusinessRegistrationNumber())) {
            throw new IllegalArgumentException("Số đăng ký kinh doanh đã tồn tại trong hệ thống.");
        }
        if (user.getPartnerOrganization() != null) {
            throw new EntityNotFoundException("Đã có tài khoản");
        }
        try {
            PartnerOrganization organization = partnerOrganizationMapper.toEntity(req);
            organization.setUser(user);
            organization.setStatus(PendingStatus.PENDING);
            partnerOrganizationRepository.save(organization);
            partnerOrganizationRepository.flush();
            return partnerOrganizationMapper.toDTO(organization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Transactional
    public PartnerOrganizationDTO updatePartner(PartnerOrganizationReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganization organization = user.getPartnerOrganization();
        if (organization == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        if (partnerOrganizationRepository.existsByBusinessRegistrationNumberAndIdNot(
                req.getBusinessRegistrationNumber(), organization.getId())) {
            throw new IllegalArgumentException("Số đăng ký kinh doanh đã tồn tại trong hệ thống.");
        }
        if (user.getRole() == Role.ORGANIZATION && organization.getStatus() == PendingStatus.APPROVED) {
            throw new IllegalStateException("Bạn không có quyền cập nhật thông tin đối tác này.");
        }
        try {
            partnerOrganizationMapper.updateEntityFromRequest(req, organization);
            organization.setStatus(PendingStatus.PENDING);
            organization.setAdminNote("");
            partnerOrganizationRepository.save(organization);
            partnerOrganizationRepository.flush();
            return partnerOrganizationMapper.toDTO(organization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Transactional
    public PartnerOrganizationDTO updatePartnerFromUser(PartnerUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganization partnerOrganization = user.getPartnerOrganization();
        if (partnerOrganization == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            Optional<PartnerOrganizationUpdate> optionalUpdate =
                    partnerOrganizationUpdateRepository.findByPartnerOrganization(partnerOrganization);
            PartnerOrganizationUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                partnerOrganizationMapper.updateUpdateFromRequest(req, update);
            } else {
                update = partnerOrganizationMapper.toUpdate(req);
                update.setPartnerOrganization(partnerOrganization);
            }
            update.setStatus(PendingStatus.PENDING);
            update.setAdminNote("");
            partnerOrganizationUpdateRepository.save(update);
            partnerOrganizationUpdateRepository.flush();
            return partnerOrganizationMapper.toDTO(partnerOrganization);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý cập nhật đối tác: " + e.getMessage(), e);
        }
    }


    @Transactional
    public List<PartnerOrganizationPendingDTO> getPendingPartnerOrganizationUpdates() {
        List<PartnerOrganizationUpdate> pendingUpdates =
                partnerOrganizationUpdateRepository.findByStatus(PendingStatus.PENDING);

        return pendingUpdates.stream()
                .map(update -> {
                    PartnerOrganization organization = update.getPartnerOrganization();
                    PartnerOrganizationDTO orgDTO = partnerOrganizationMapper.toDTO(organization);
                    PartnerOrganizationUpdateDTO updateDTO = partnerOrganizationMapper.toDTO(update);
                    System.out.println("Pending Update: " + update);
                    return PartnerOrganizationPendingDTO.builder()
                            .partnerOrganization(orgDTO)
                            .partnerOrganizationUpdate(updateDTO)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PartnerOrganizationDTO> getPendingPartnerOrganizationCreate() {
        try {
            List<PartnerOrganization> pendingOrganizations =
                    partnerOrganizationRepository.findByStatus(PendingStatus.PENDING);

            return pendingOrganizations.stream()
                    .map(partnerOrganizationMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public PartnerOrganizationDTO getPendingPartnerProfile(User user) {
        PartnerOrganization partnerOrganization = user.getPartnerOrganization();
        if (partnerOrganization == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        try {
            return partnerOrganizationMapper.toDTO(partnerOrganization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
