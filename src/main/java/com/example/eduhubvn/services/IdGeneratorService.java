package com.example.eduhubvn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eduhubvn.repositories.TrainingProgramRepository;

@Service
public class IdGeneratorService {
    
    @Autowired
    private TrainingProgramRepository trainingProgramRepository;
    
    public synchronized String generateTrainingProgramId() {
        // Tìm training program ID cao nhất hiện tại
        String lastId = trainingProgramRepository.findLastTrainingProgramId();
        
        int nextNumber = 1;
        if (lastId != null && lastId.startsWith("KH-")) {
            try {
                // Lấy phần số từ "KH-xxx"
                String numberPart = lastId.substring(3);
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                // Nếu không parse được thì bắt đầu từ 1
                nextNumber = 1;
            }
        }
        
        // Format thành KH-001, KH-002, ...
        return String.format("KH-%03d", nextNumber);
    }
}
