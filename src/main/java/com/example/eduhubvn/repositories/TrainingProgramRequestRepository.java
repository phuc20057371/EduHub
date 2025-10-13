package com.example.eduhubvn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eduhubvn.entities.TrainingProgramRequest;

@Repository
public interface TrainingProgramRequestRepository extends JpaRepository<TrainingProgramRequest, UUID> {
    
}