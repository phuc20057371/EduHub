package com.example.eduhubvn.dtos.subadmin.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubAdminRequest {
    private String email;
    private String password;
}
