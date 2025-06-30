package com.example.eduhubvn.dtos.lecturer.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RejectPendingLecturerRequest {
    private Integer pendingLecturerId;
    private String response; // Lý do tổng thể

    private List<RejectedDegreeInfo> rejectedDegrees;
    private List<RejectedCertificationInfo> rejectedCertifications;

    @Data
    public static class RejectedDegreeInfo {
        private Integer id;
        private String reason;
    }

    @Data
    public static class RejectedCertificationInfo {
        private Integer id;
        private String reason;
    }

}
