package com.example.eduhubvn.dtos;

import com.example.eduhubvn.dtos.lecturer.LecturerInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestFromLecturer<T> {
    private T content;
    private LecturerInfoDTO lecturerInfo;
    private RequestLecturerType type;
    private RequestLabel label;
    private LocalDateTime date;
}