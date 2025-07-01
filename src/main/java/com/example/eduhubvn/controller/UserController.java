package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.ApiResponse;
import com.example.eduhubvn.dtos.FileResponse;
import com.example.eduhubvn.dtos.UserProfileDTO;
import com.example.eduhubvn.dtos.partner.PartnerOrganizationDTO;
import com.example.eduhubvn.services.GoogleDriveService;
import com.example.eduhubvn.services.PartnerOrganizationService;
import com.example.eduhubvn.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final GoogleDriveService googleDriveService;
    private final UserService userService;

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "File is empty";
        } else {
            File tempFile = File.createTempFile("temp", file.getOriginalFilename());
            file.transferTo(tempFile);
            FileResponse fileResponse = googleDriveService.uploadFileToGoogleDrive(tempFile);
            System.out.println(fileResponse);
            return fileResponse;
        }
    }
    @GetMapping("/current-user")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getCurrentUser() {
        UserProfileDTO profile = userService.getUserProfile();
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin người dùng thành công", profile));
    }

}
