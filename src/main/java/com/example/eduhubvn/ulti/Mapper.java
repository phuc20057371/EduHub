package com.example.eduhubvn.ulti;


import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import com.example.eduhubvn.entities.Lecturer;
import org.springframework.stereotype.Component;

@Component
public class Mapper {


    public static LecturerInfoDTO mapToLecturerInfoDTO(Lecturer lecturer) {
        if (lecturer == null) {
            return null;
        }

        return LecturerInfoDTO.builder()
                .id(lecturer.getId())
                .citizenId(lecturer.getCitizenId())
                .email(lecturer.getUser() != null ? lecturer.getUser().getEmail() : null)
                .phoneNumber(lecturer.getPhoneNumber())
                .fullName(lecturer.getFullName())
                .dateOfBirth(lecturer.getDateOfBirth())
                .gender(lecturer.getGender())
                .bio(lecturer.getBio())
                .address(lecturer.getAddress())
                .avatarUrl(lecturer.getAvatarUrl())
                .academicRank(lecturer.getAcademicRank())
                .specialization(lecturer.getSpecialization())
                .experienceYears(lecturer.getExperienceYears())
                .jobField(lecturer.getJobField())
                .adminNote(lecturer.getAdminNote())
                .status(lecturer.getStatus())
                .createdAt(lecturer.getCreatedAt())
                .updatedAt(lecturer.getUpdatedAt())
                .build();
    }
}
