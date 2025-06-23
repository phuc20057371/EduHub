package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.PendingLecturerRequest;
import com.example.eduhubvn.dtos.PendingLecturerResponse;
import com.example.eduhubvn.entities.PendingLecturer;
import com.example.eduhubvn.services.PendingLecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import static com.example.eduhubvn.mapper.LecturerMapper.toPendingLecturerResponse;


@RestController
@RequestMapping("/api/v1/pending-lecturer")
@RequiredArgsConstructor
public class PendingLecturerController {
    private final PendingLecturerService pendingLecturerService;


    @PostMapping("/create")
    public ResponseEntity<PendingLecturerResponse> createPendingLecturer(@RequestBody PendingLecturerRequest request) {
        PendingLecturer pending = pendingLecturerService.createPendingLecturer(request);
        PendingLecturerResponse response = toPendingLecturerResponse(pending); // gọi static method
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-email")
    public ResponseEntity<PendingLecturerResponse> getByEmail(@RequestParam String email) {
        PendingLecturer pending = pendingLecturerService.getPendingLecturerByEmail(email);
        return ResponseEntity.ok(toPendingLecturerResponse(pending));
    }

    @GetMapping("/current-user-email")
    public ResponseEntity<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
        }

        String email = authentication.getName();
        return ResponseEntity.ok(email);
    }

}
