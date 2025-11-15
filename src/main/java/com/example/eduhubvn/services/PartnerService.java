package com.example.eduhubvn.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.eduhubvn.dtos.MessageSocket;
import com.example.eduhubvn.dtos.MessageSocketType;
import com.example.eduhubvn.dtos.partner.PartnerInfoDTO;
import com.example.eduhubvn.dtos.partner.PartnerDTO;
import com.example.eduhubvn.dtos.partner.PartnerPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerUpdateDTO;
import com.example.eduhubvn.dtos.partner.PartnerProfileDTO;
import com.example.eduhubvn.dtos.partner.request.PartnerCreateReq;
import com.example.eduhubvn.dtos.partner.request.PartnerUpdateReq;
import com.example.eduhubvn.dtos.program.TrainingProgramRequestDTO;
import com.example.eduhubvn.dtos.program.request.TrainingProgramRequestReq;
import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PartnerOrganizationUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.Role;
import com.example.eduhubvn.entities.TrainingProgramRequest;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.PartneMapper;
import com.example.eduhubvn.mapper.TrainingProgramRequestMapper;
import com.example.eduhubvn.repositories.PartnerRepository;
import com.example.eduhubvn.repositories.PartnerUpdateRepository;
import com.example.eduhubvn.repositories.TrainingProgramRequestRepository;
import com.example.eduhubvn.ulti.Mapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerOrganizationRepository;
    private final PartnerUpdateRepository partnerOrganizationUpdateRepository;
    private final TrainingProgramRequestRepository trainingProgramRequestRepository;

    private final PartneMapper partnerOrganizationMapper;
    private final TrainingProgramRequestMapper trainingProgramRequestMapper;

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public PartnerDTO createPartnerFromUser(PartnerCreateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if (partnerOrganizationRepository.existsByBusinessRegistrationNumber(req.getBusinessRegistrationNumber())) {
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
            PartnerDTO dto = partnerOrganizationMapper.toDTO(organization);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.CREATE_PARTNER, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public PartnerDTO updatePartner(PartnerCreateReq req, User user) {
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
            PartnerDTO dto = partnerOrganizationMapper.toDTO(organization);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.UPDATE_PARTNER, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public PartnerDTO updatePartnerFromUser(PartnerUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        PartnerOrganization partnerOrganization = user.getPartnerOrganization();
        if (partnerOrganization == null) {
            throw new IllegalStateException("Không có quyền truy cập.");
        }
        try {
            Optional<PartnerOrganizationUpdate> optionalUpdate = partnerOrganizationUpdateRepository
                    .findByPartnerOrganization(partnerOrganization);
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
            PartnerDTO dto = partnerOrganizationMapper.toDTO(partnerOrganization);
            messagingTemplate.convertAndSend("/topic/ADMIN",
                    new MessageSocket(MessageSocketType.EDIT_PARTNER, dto));
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý cập nhật đối tác: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<PartnerPendingDTO> getPendingPartnerOrganizationUpdates() {
        List<PartnerOrganizationUpdate> pendingUpdates = partnerOrganizationUpdateRepository
                .findByStatus(PendingStatus.PENDING);

        return pendingUpdates.stream()
                .filter(update -> update.getPartnerOrganization().getStatus() == PendingStatus.APPROVED && !update.getPartnerOrganization().isHidden()) 
                .map(update -> {
                    PartnerOrganization organization = update.getPartnerOrganization();
                    PartnerDTO orgDTO = partnerOrganizationMapper.toDTO(organization);
                    PartnerUpdateDTO updateDTO = partnerOrganizationMapper.toDTO(update);
                    return PartnerPendingDTO.builder()
                            .partnerOrganization(orgDTO)
                            .partnerOrganizationUpdate(updateDTO)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PartnerInfoDTO> getPendingPartnerOrganizationCreate() {
        try {
            List<PartnerOrganization> pendingOrganizations = partnerOrganizationRepository
                    .findByStatus(PendingStatus.PENDING);

            return pendingOrganizations.stream()
                    .map(Mapper::mapToPartnerInfoDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public PartnerDTO getPendingPartnerProfile(User user) {
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

    public PartnerProfileDTO getPartnerProfile(User user) {
        PartnerOrganization partnerOrganization = user.getPartnerOrganization();
        if (partnerOrganization == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        try {
            return PartnerProfileDTO.builder()
                    .partner(Mapper.mapToPartnerInfoDTO(partnerOrganization))
                    .partnerUpdate(partnerOrganizationMapper.toDTO(partnerOrganization.getPartnerUpdate()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public PartnerDTO updatePartnerLogo(String logoUrl, User user) {
        if (logoUrl == null || logoUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL logo không được trống.");
        }
        PartnerOrganization partnerOrganization = user.getPartnerOrganization();
        if (partnerOrganization == null) {
            throw new IllegalStateException("Không tìm thấy thông tin tổ chức đối tác.");
        }
        try {
            partnerOrganization.setLogoUrl(logoUrl.trim());
            partnerOrganizationRepository.save(partnerOrganization);
            partnerOrganizationRepository.flush();
            return partnerOrganizationMapper.toDTO(partnerOrganization);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật logo: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<TrainingProgramRequestDTO> getAllTrainingProgramRequests(User user) {
        PartnerOrganization partnerOrganization = user.getPartnerOrganization();
        if (partnerOrganization == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        try {
            List<TrainingProgramRequest> requests = trainingProgramRequestRepository.findByPartnerOrganization(partnerOrganization);
            return requests.stream()
                    .map(trainingProgramRequestMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public TrainingProgramRequestDTO createTrainingProgramRequest(TrainingProgramRequestReq request, User user) {
        PartnerOrganization partnerOrganization = user.getPartnerOrganization();
        if (partnerOrganization == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        try {
            TrainingProgramRequest programRequest = TrainingProgramRequest.builder()
                    .partnerOrganization(partnerOrganization)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .status(PendingStatus.PENDING)
                    .fileUrl(request.getFileUrl())
                    .build();
            trainingProgramRequestRepository.save(programRequest);
            trainingProgramRequestRepository.flush();
            return trainingProgramRequestMapper.toDto(programRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
