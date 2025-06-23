package com.example.eduhubvn.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private int status;
    private String message;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private long size;
}
