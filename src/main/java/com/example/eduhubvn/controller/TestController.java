package com.example.eduhubvn.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

        @RequestMapping("/hello")
        public String hello(){
            return "Hello World";
        }
}
