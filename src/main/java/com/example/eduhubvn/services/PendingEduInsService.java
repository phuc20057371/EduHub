package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.edu.PendingEducationInstitutionDTO;
import com.example.eduhubvn.entities.PendingEducationInstitution;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.repositories.PendingEducationInstitutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PendingEduInsService {
    private final PendingEducationInstitutionRepository pendingEducationInstitutionRepository;

    @Transactional
    public PendingEducationInstitutionDTO createPendingEduIns(PendingEducationInstitutionDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Thông tin yêu cầu không được để trống.");
        }
        if (request.getBusinessRegistrationNumber() == null || request.getBusinessRegistrationNumber().isBlank()) {
            throw new IllegalArgumentException("Số đăng ký kinh doanh không được để trống.");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy người dùng đang đăng nhập.");
        }
        if (user.getPendingEducationInstitution() != null) {
            throw new IllegalStateException("Bạn đã có hồ sơ cơ sở giáo dục đang chờ duyệt.");
        }
        PendingEducationInstitution pending = PendingEducationInstitution.builder()
                .businessRegistrationNumber(request.getBusinessRegistrationNumber())
                .institutionName(request.getInstitutionName())
                .institutionType(request.getInstitutionType())
                .phoneNumber(request.getPhoneNumber())
                .website(request.getWebsite())
                .address(request.getAddress())
                .representativeName(request.getRepresentativeName())
                .position(request.getPosition())
                .description(request.getDescription())
                .establishedYear(request.getEstablishedYear())
                .logoUrl(request.getLogoUrl() != null ? request.getLogoUrl() : "https://picsum.photos/200")
                .status(PendingStatus.PENDING)
                .reason("")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        PendingEducationInstitution saved = pendingEducationInstitutionRepository.save(pending);
        return EducationInstitutionMapper.toDTO(saved);
    }

}
