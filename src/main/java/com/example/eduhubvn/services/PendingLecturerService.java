package com.example.eduhubvn.services;

import com.example.eduhubvn.dtos.lecturer.PendingLecturerRequest;
import com.example.eduhubvn.dtos.lecturer.PendingLecturerResponse;
import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.mapper.LecturerMapper;
import com.example.eduhubvn.repositories.PendingLecturerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PendingLecturerService {
    private final PendingLecturerRepository repository;


    @Transactional
    public PendingLecturerResponse createPendingLecturer(PendingLecturerRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Không tìm thấy user đang đăng nhập");
        }

        if (user.getPendingLecturer() != null) {
            throw new IllegalStateException("PendingLecturer profile already exists for this user.");
        }
        PendingLecturer pending = PendingLecturer.builder()
                .user(user)
                .citizenID(request.getCitizenID())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bio(request.getBio())
                .address(request.getAddress())
                .avatarUrl(request.getAvatarUrl())
                .academicRank(request.getAcademicRank())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .status(PendingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .pendingCertifications(new ArrayList<>())
                .pendingDegrees(new ArrayList<>())
                .build();

        // Gán degrees
        if (request.getPendingDegrees() != null) {
            request.getPendingDegrees().forEach(degreeReq -> {
                PendingDegree degree = PendingDegree.builder()
                        .name(degreeReq.getName())
                        .major(degreeReq.getMajor())
                        .institution(degreeReq.getInstitution())
                        .startYear(degreeReq.getStartYear())
                        .graduationYear(degreeReq.getGraduationYear())
                        .level(degreeReq.getLevel())
                        .url(degreeReq.getUrl())
                        .referenceID(degreeReq.getReferenceID())
                        .reqNo(degreeReq.getReqNo())
                        .description(degreeReq.getDescription())
                        .status(PendingStatus.PENDING)
                        .submittedAt(LocalDateTime.now())
                        .pendingLecturer(pending)
                        .build();
                pending.getPendingDegrees().add(degree);
            });
        }

        // Gán certifications
        if (request.getPendingCertifications() != null) {
            request.getPendingCertifications().forEach(certReq -> {
                PendingCertification cert = PendingCertification.builder()
                        .name(certReq.getName())
                        .issuedBy(certReq.getIssuedBy())
                        .issueDate(certReq.getIssueDate())
                        .expiryDate(certReq.getExpiryDate())
                        .certificateUrl(certReq.getCertificateUrl())
                        .level(certReq.getLevel())
                        .description(certReq.getDescription())
                        .status(PendingStatus.PENDING)
                        .submittedAt(LocalDateTime.now())
                        .pendingLecturer(pending)
                        .build();
                pending.getPendingCertifications().add(cert);
            });
        }
        PendingLecturer saved = repository.save(pending);

        return LecturerMapper.toPendingLecturerResponse(saved);

    }
    public PendingLecturer getPendingLecturerByEmail(String email) {
        return repository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found with email: " + email));
    }

//    @Transactional
//    public PendingLecturer getByEmail(String email) {
//        return repository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("PendingLecturer with email " + email + " not found"));
//    }
//    public boolean isPendingLecturerByEmail(String email) {
//        return repository.existsByEmail(email);
//    }
}
