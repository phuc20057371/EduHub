package com.example.eduhubvn.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturer")
@PreAuthorize("hasRole('LECTURER')")
public class LecturerController {
    @GetMapping
    @PreAuthorize("hasAuthority('lecturer:read')")
    public String get(){
        return "GET:: LECTURER";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('lecturer:create')")
    public String post(){
        return "POST:: LECTURER";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('lecturer:update')")
    public String put(){
        return "PUT:: LECTURER";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('lecturer:delete')")
    public String delete(){
        return "DELETE:: LECTURER";
    }
}
