package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.AllPendingEntityDTO;
import com.example.eduhubvn.dtos.AllPendingUpdateDTO;
import com.example.eduhubvn.dtos.RejectReq;
import com.example.eduhubvn.dtos.edu.EducationInstitutionDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionPendingDTO;
import com.example.eduhubvn.dtos.edu.EducationInstitutionUpdateDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerPendingDTO;
import com.example.eduhubvn.dtos.lecturer.LecturerUpdateDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationPendingDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationUpdateDTO;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.*;
import com.example.eduhubvn.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final LecturerRepository lecturerRepository;
    private final PartnerOrganizationRepository partnerOrganizationRepository;
    private final EducationInstitutionRepository educationInstitutionRepository;

    private final LecturerUpdateRepository lecturerUpdateRepository;
    private final PartnerOrganizationUpdateRepository partnerOrganizationUpdateRepository;
    private final EducationInstitutionUpdateRepository educationInstitutionUpdateRepository;

    private final LecturerMapper lecturerMapper;
    private final LecturerUpdateMapper lecturerUpdateMapper;
    private final PartnerOrganizationMapper partnerOrganizationMapper;
    private final PartnerOrganizationUpdateMapper partnerOrganizationUpdateMapper;
    private final EducationInstitutionMapper educationInstitutionMapper;
    private final EducationInstitutionUpdateMapper educationInstitutionUpdateMapper;
    private final PageableArgumentResolver pageableArgumentResolver;


    @Transactional
    public AllPendingUpdateDTO getAllPendingUpdates() {
        List<LecturerUpdate> lecturerUpdates = lecturerUpdateRepository.findByStatus(PendingStatus.PENDING);
        List<PartnerOrganizationUpdate> partnerUpdates = partnerOrganizationUpdateRepository.findByStatus(PendingStatus.PENDING);
        List<EducationInstitutionUpdate> eduInsUpdates = educationInstitutionUpdateRepository.findByStatus(PendingStatus.PENDING);

        List<LecturerPendingDTO> lecturerDTOs = lecturerUpdates.stream()
                .map(update -> new LecturerPendingDTO(
                        lecturerMapper.toDTO(update.getLecturer()),
                        lecturerUpdateMapper.toDTO(update)
                )).toList();

        List<PartnerOrganizationPendingDTO> partnerDTOs = partnerUpdates.stream()
                .map(update -> new PartnerOrganizationPendingDTO(
                        partnerOrganizationMapper.toDTO(update.getPartnerOrganization()),
                        partnerOrganizationUpdateMapper.toDTO(update)
                )).toList();

        List<EducationInstitutionPendingDTO> eduDTOs = eduInsUpdates.stream()
                .map(update -> new EducationInstitutionPendingDTO(
                        educationInstitutionMapper.toDTO(update.getEducationInstitution()),
                        educationInstitutionUpdateMapper.toDTO(update)
                )).toList();

        return AllPendingUpdateDTO.builder()
                .lecturerUpdates(lecturerDTOs)
                .partnerUpdates(partnerDTOs)
                .educationInstitutionUpdates(eduDTOs)
                .build();
    }

    public AllPendingEntityDTO getAllPendingEntities() {
        List<LecturerDTO> lecturers = lecturerRepository.findByStatus(PendingStatus.PENDING).stream()
                .map(lecturerMapper::toDTO)
                .toList();

        List<PartnerOrganizationDTO> partners = partnerOrganizationRepository.findByStatus(PendingStatus.PENDING).stream()
                .map(partnerOrganizationMapper::toDTO)
                .toList();

        List<EducationInstitutionDTO> institutions = educationInstitutionRepository.findByStatus(PendingStatus.PENDING).stream()
                .map(educationInstitutionMapper::toDTO)
                .toList();

        return AllPendingEntityDTO.builder()
                .lecturers(lecturers)
                .partnerOrganizations(partners)
                .educationInstitutions(institutions)
                .build();
    }

    @Transactional
    public LecturerDTO approveLecturerUpdate(Integer updateId) {
        LecturerUpdate update = lecturerUpdateRepository.findByIdAndStatus(updateId, PendingStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản cập nhật đang chờ duyệt."));

        try {
            Lecturer lecturer = update.getLecturer();
            lecturer.setFullName(update.getFullName());
            lecturer.setDateOfBirth(update.getDateOfBirth());
            lecturer.setGender(update.getGender());
            lecturer.setBio(update.getBio());
            lecturer.setAddress(update.getAddress());
            lecturer.setAvatarUrl(update.getAvatarUrl());
            lecturer.setAcademicRank(update.getAcademicRank());
            lecturer.setSpecialization(update.getSpecialization());
            lecturer.setExperienceYears(update.getExperienceYears());

            update.setStatus(PendingStatus.APPROVED);

            lecturerRepository.save(lecturer);
            lecturerUpdateRepository.save(update);
            lecturerRepository.flush();
            lecturerUpdateRepository.flush();
            return  lecturerMapper.toDTO(lecturer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public EducationInstitutionDTO approveEduInsUpdate(Integer updateId) {
        EducationInstitutionUpdate update = educationInstitutionUpdateRepository.findByIdAndStatus(updateId, PendingStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản cập nhật đang chờ duyệt."));
        try {
            EducationInstitution educationInstitution = update.getEducationInstitution();
            educationInstitution.setInstitutionName(update.getInstitutionName());
            educationInstitution.setPhoneNumber(update.getPhoneNumber());
            educationInstitution.setWebsite(update.getWebsite());
            educationInstitution.setAddress(update.getAddress());
            educationInstitution.setRepresentativeName(update.getRepresentativeName());
            educationInstitution.setPosition(update.getPosition());
            educationInstitution.setDescription(update.getDescription());
            educationInstitution.setLogoUrl(update.getLogoUrl());
            educationInstitution.setEstablishedYear(update.getEstablishedYear());

            update.setStatus(PendingStatus.APPROVED);
            educationInstitutionRepository.save(educationInstitution);
            educationInstitutionUpdateRepository.save(update);
            educationInstitutionRepository.flush();
            educationInstitutionUpdateRepository.flush();
            return educationInstitutionMapper.toDTO(educationInstitution);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public PartnerOrganizationDTO approvePartnerUpdate(Integer updateId) {
        PartnerOrganizationUpdate update = partnerOrganizationUpdateRepository.findByIdAndStatus(updateId, PendingStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản cập nhật đang chờ duyệt."));
        try {
            PartnerOrganization partnerOrganization = update.getPartnerOrganization();
            partnerOrganization.setOrganizationName(update.getOrganizationName());
            partnerOrganization.setIndustry(update.getIndustry());
            partnerOrganization.setPhoneNumber(update.getPhoneNumber());
            partnerOrganization.setWebsite(update.getWebsite());
            partnerOrganization.setAddress(update.getAddress());
            partnerOrganization.setRepresentativeName(update.getRepresentativeName());
            partnerOrganization.setPosition(update.getPosition());
            partnerOrganization.setDescription(update.getDescription());
            partnerOrganization.setLogoUrl(update.getLogoUrl());
            partnerOrganization.setEstablishedYear(update.getEstablishedYear());

            update.setStatus(PendingStatus.APPROVED);
            partnerOrganizationRepository.save(partnerOrganization);
            partnerOrganizationUpdateRepository.save(update);
            partnerOrganizationRepository.flush();
            partnerOrganizationUpdateRepository.flush();
            return partnerOrganizationMapper.toDTO(partnerOrganization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public LecturerUpdateDTO rejectLecturerUpdate(RejectReq req) {
        LecturerUpdate update = lecturerUpdateRepository.findByIdAndStatus(req.getId(), PendingStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản cập nhật đang chờ duyệt."));
        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            lecturerUpdateRepository.save(update);
            lecturerUpdateRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lecturerUpdateMapper.toDTO(update);
    }


    public EducationInstitutionUpdateDTO rejectEduInsUpdate(RejectReq req) {
        EducationInstitutionUpdate update = educationInstitutionUpdateRepository.findByIdAndStatus(req.getId(), PendingStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản cập nhật đang chờ duyệt."));
        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            educationInstitutionUpdateRepository.save(update);
            educationInstitutionUpdateRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return educationInstitutionUpdateMapper.toDTO(update);
    }

    public PartnerOrganizationUpdateDTO rejectPartnerUpdate(RejectReq req) {
        PartnerOrganizationUpdate update = partnerOrganizationUpdateRepository.findByIdAndStatus(req.getId(), PendingStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản cập nhật đang chờ duyệt."));
        try {
            update.setStatus(PendingStatus.REJECTED);
            update.setAdminNote(req.getAdminNote());
            partnerOrganizationUpdateRepository.save(update);
            partnerOrganizationUpdateRepository.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return partnerOrganizationUpdateMapper.toDTO(update);
    }



}
