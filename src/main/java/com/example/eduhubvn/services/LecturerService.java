package com.example.eduhubvn.services;

import com.example.eduhubvn.entities.*;
import com.example.eduhubvn.repositories.LecturerRepository;
import com.example.eduhubvn.repositories.PendingLecturerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private  final LecturerRepository lecturerRepository;
    private final PendingLecturerRepository pendingLecturerRepository;

    @Transactional
    public Lecturer createLecturerFromPending(Integer pendingId) {
        PendingLecturer pending = pendingLecturerRepository.findById(pendingId)
                .orElseThrow(() -> new RuntimeException("PendingLecturer not found"));

        User user = pending.getUser();

        if (user.getLecturer() != null) {
            throw new IllegalStateException("User already has a Lecturer profile.");
        }

        user.setRole(Role.LECTURER);

        pending.setStatus(PendingStatus.APPROVED);
        pending.setUpdatedAt(LocalDateTime.now());

        if (pending.getPendingDegrees() != null) {
            pending.getPendingDegrees().forEach(degree -> {
                degree.setStatus(PendingStatus.APPROVED);
                degree.setReviewedAt(LocalDateTime.now());
                degree.setUpdatedAt(LocalDateTime.now());
            });
        }

        if (pending.getPendingCertifications() != null) {
            pending.getPendingCertifications().forEach(cert -> {
                cert.setStatus(PendingStatus.APPROVED);
                cert.setReviewedAt(LocalDateTime.now());
                cert.setUpdatedAt(LocalDateTime.now());
            });
        }

        Lecturer lecturer = Lecturer.builder()
                .user(user)
                .fullName(pending.getFullName())
                .citizenID(pending.getCitizenID())
                .dateOfBirth(pending.getDateOfBirth())
                .gender(pending.getGender())
                .bio(pending.getBio())
                .address(pending.getAddress())
                .avatarUrl(pending.getAvatarUrl())
                .academicRank(pending.getAcademicRank())
                .specialization(pending.getSpecialization())
                .experienceYears(pending.getExperienceYears())
                .certifications(new ArrayList<>())
                .degrees(new ArrayList<>())
                .build();
        if (pending.getPendingDegrees() != null) {
            List<Degree> degrees = pending.getPendingDegrees().stream().map(p -> {
                p.setStatus(PendingStatus.APPROVED);
                p.setReviewedAt(LocalDateTime.now());
                p.setUpdatedAt(LocalDateTime.now());

                return Degree.builder()
                        .lecturer(lecturer)
                        .name(p.getName())
                        .major(p.getMajor())
                        .institution(p.getInstitution())
                        .startYear(p.getStartYear())
                        .graduationYear(p.getGraduationYear())
                        .level(p.getLevel())
                        .url(p.getUrl())
                        .referenceID(p.getReferenceID())
                        .description(p.getDescription())
                        .build();
            }).toList();
            lecturer.setDegrees(degrees);
        }
        if (pending.getPendingCertifications() != null) {
            List<Certification> certifications = pending.getPendingCertifications().stream().map(p -> {
                p.setStatus(PendingStatus.APPROVED);
                p.setReviewedAt(LocalDateTime.now());
                p.setUpdatedAt(LocalDateTime.now());

                return Certification.builder()
                        .lecturer(lecturer)
                        .name(p.getName())
                        .issuedBy(p.getIssuedBy())
                        .issueDate(p.getIssueDate())
                        .expiryDate(p.getExpiryDate())
                        .certificateUrl(p.getCertificateUrl())
                        .level(p.getLevel())
                        .description(p.getDescription())
                        .build();
            }).toList();
            lecturer.setCertifications(certifications);
        }
        // Lưu Lecturer
        lecturerRepository.save(lecturer);

        return lecturer;
    }

}
