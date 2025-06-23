package com.example.eduhubvn.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    private String otp;
    private LocalDateTime expiryDate;
}
