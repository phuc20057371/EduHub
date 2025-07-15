package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.edu.request.EduInsUpdateReq;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.repositories.EducationInstitutionRepository;
import com.example.eduhubvn.repositories.EducationInstitutionUpdateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationInstitutionService {
    private final EducationInstitutionRepository educationInstitutionRepository;
    private final EducationInstitutionUpdateRepository educationInstitutionUpdateRepository;

    private final EducationInstitutionMapper educationInstitutionMapper;

    /// GET
    @Transactional
    public List<EducationInstitutionPendingDTO> getPendingEducationInstitutionUpdates() {
        try {
            List<EducationInstitutionUpdate> pendingUpdates =
                    educationInstitutionUpdateRepository.findByStatus(PendingStatus.PENDING);

            return pendingUpdates.stream()
                    .map(update -> {
                        EducationInstitution institution = update.getEducationInstitution();
                        EducationInstitutionDTO institutionDTO = educationInstitutionMapper.toDTO(institution);
                        EducationInstitutionUpdateDTO updateDTO = educationInstitutionMapper.toDTO(update);
                        return new EducationInstitutionPendingDTO(institutionDTO, updateDTO);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EducationInstitutionDTO createEduInsFromUser(EducationInstitutionReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if (user.getEducationInstitution() != null) {
            throw new EntityNotFoundException("Đã có tài khoản");
        }
        try {
            EducationInstitution institution = educationInstitutionMapper.toEntity(req);
            institution.setUser(user);
            institution.setStatus(PendingStatus.PENDING);
            educationInstitutionRepository.save(institution);
            educationInstitutionRepository.flush();
            return educationInstitutionMapper.toDTO(institution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EducationInstitutionDTO updateEduins(EducationInstitutionReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution institution = user.getEducationInstitution();
        if (institution == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        try {
            educationInstitutionMapper.updateEntityFromRequest(req, institution);
            institution.setStatus(PendingStatus.PENDING);
            institution.setAdminNote("");
            educationInstitutionRepository.save(institution);
            educationInstitutionRepository.flush();
            return educationInstitutionMapper.toDTO(institution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public EduInsUpdateReq updateEduInsFromUser(EduInsUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution institution = user.getEducationInstitution();
        if (institution == null) {
            throw new EntityNotFoundException("Không có quyền truy cập");
        }
        try {
            Optional<EducationInstitutionUpdate> optionalUpdate =
                    educationInstitutionUpdateRepository.findByEducationInstitution(institution);
            EducationInstitutionUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                educationInstitutionMapper.updateUpdateFromRequest(req, update);
            } else {
                update = educationInstitutionMapper.toUpdate(req);
                update.setEducationInstitution(institution);
            }
            update.setStatus(PendingStatus.PENDING);
            educationInstitutionUpdateRepository.save(update);
            educationInstitutionUpdateRepository.flush();
            return req;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý cập nhật: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<EducationInstitutionDTO> getPendingEducationInstitutionCreate() {
        try {
            List<EducationInstitution> pendingInstitutions =
                    educationInstitutionRepository.findByStatus(PendingStatus.PENDING);

            return pendingInstitutions.stream()
                    .map(educationInstitutionMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
