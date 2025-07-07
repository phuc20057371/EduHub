package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.edu.request.EduInsUpdateReq;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.mapper.EducationInstitutionUpdateMapper;
import com.example.eduhubvn.repositories.EducationInstitutionRepository;
import com.example.eduhubvn.repositories.EducationInstitutionUpdateRepository;
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
    private final EducationInstitutionUpdateMapper educationInstitutionUpdateMapper;


    @Transactional
    public EducationInstitutionDTO createEduInsFromUser(EducationInstitutionReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        if (user.getEducationInstitution() != null) {
            throw new IllegalStateException("Tài khoản này đã đăng ký trung tâm đào tạo.");
        }
        EducationInstitution institution = educationInstitutionMapper.toEntity(req);
        institution.setUser(user);
        institution.setStatus(PendingStatus.PENDING);
        EducationInstitution saved = educationInstitutionRepository.save(institution);

        educationInstitutionRepository.flush();
        return educationInstitutionMapper.toDTO(saved);
    }

    public EducationInstitutionDTO approveEduIns(IdRequest req) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution educationInstitution = educationInstitutionRepository.findById(req.getId()).orElse(null);
        if(educationInstitution == null) {
            throw new IllegalStateException("Tài khoản này chưa đăng ký trung tâm đào tạo.");
        }
        educationInstitution.getUser().setRole(Role.SCHOOL);
        educationInstitution.setStatus(PendingStatus.APPROVED);
        EducationInstitution saved = educationInstitutionRepository.save(educationInstitution);
        educationInstitutionRepository.flush();
        return educationInstitutionMapper.toDTO(saved);
    }

    @Transactional
    public EduInsUpdateReq updateEduInsFromUser(EduInsUpdateReq req, User user) {
        if (req == null) {
            throw new IllegalStateException("Dữ liệu yêu cầu không được trống.");
        }
        EducationInstitution institution = user.getEducationInstitution();
        if (institution == null) {
            throw new IllegalStateException("Người dùng chưa đăng ký tổ chức giáo dục.");
        }
        try {
            Optional<EducationInstitutionUpdate> optionalUpdate =
                    educationInstitutionUpdateRepository.findByEducationInstitution(institution);

            EducationInstitutionUpdate update;
            if (optionalUpdate.isPresent()) {
                update = optionalUpdate.get();
                educationInstitutionUpdateMapper.updateEntityFromDto(req, update);
            } else {
                update = educationInstitutionUpdateMapper.toUpdate(req);
                update.setEducationInstitution(institution);
            }
            update.setStatus(PendingStatus.PENDING);
            educationInstitutionUpdateRepository.save(update);
            educationInstitutionUpdateRepository.flush();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý cập nhật: " + e.getMessage(), e);
        }

        return req;
    }

    @Transactional
    public List<EducationInstitutionPendingDTO> getPendingEducationInstitutionUpdates() {
        List<EducationInstitutionUpdate> pendingUpdates =
                educationInstitutionUpdateRepository.findByStatus(PendingStatus.PENDING);

        return pendingUpdates.stream()
                .map(update -> {
                    EducationInstitution institution = update.getEducationInstitution();
                    EducationInstitutionDTO institutionDTO = educationInstitutionMapper.toDTO(institution);
                    EducationInstitutionUpdateDTO updateDTO = educationInstitutionUpdateMapper.toDTO(update);
                    return new EducationInstitutionPendingDTO(institutionDTO, updateDTO);
                })
                .collect(Collectors.toList());
    }

}
