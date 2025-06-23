package com.example.eduhubvn.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Integer id;
    private String email;
    private String phone;
    private String role;

    private LecturerDTO lecturer;
    private PendingLecturerDTO pendingLecturer;
}
