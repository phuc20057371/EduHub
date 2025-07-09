package com.example.eduhubvn.controller;


import com.example.eduhubvn.dtos.FileResponse;
import com.example.eduhubvn.services.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

}
