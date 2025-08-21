package com.example.eduhubvn.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @RequestMapping("/hello2")
    public String hello2() {
        return "Hello World 2";
    }

    @MessageMapping("/hello") // client gửi vào /app/hello
    @SendTo("/topic/ORGANIZATION") // server broadcast về /topic/ORGANIZATION
    public String handleHello(String payload) {
        System.out.println("Server nhận: " + payload);
        return "Server trả về: " + payload;
    }
}
