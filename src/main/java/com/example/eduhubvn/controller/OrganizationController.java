package com.example.eduhubvn.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organization")
public class OrganizationController {
    @GetMapping
    public String get(){
        return "GET:: ORGANIZATION";
    }
    @PostMapping
    public String post(){
        return "POST:: ORGANIZATION";
    }
    @PutMapping
    public String put(){
        return "PUT:: ORGANIZATION";
    }
    @DeleteMapping
    public String delete(){
        return "DELETE:: ORGANIZATION";
    }
}
