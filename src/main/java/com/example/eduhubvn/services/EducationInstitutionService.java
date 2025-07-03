package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.IdRequest;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.request.EducationInstitutionReq;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.mapper.EducationInstitutionMapper;
import com.example.eduhubvn.repositories.EducationInstitutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationInstitutionService {
    private final EducationInstitutionRepository educationInstitutionRepository;
    private final EducationInstitutionMapper educationInstitutionMapper;


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
        educationInstitution.setStatus(PendingStatus.APPROVED);
        EducationInstitution saved = educationInstitutionRepository.save(educationInstitution);
        educationInstitutionRepository.flush();
        return educationInstitutionMapper.toDTO(saved);
    }
}
