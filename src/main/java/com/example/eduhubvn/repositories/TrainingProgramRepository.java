package com.example.eduhubvn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.eduhubvn.entities.TrainingProgram;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, UUID> {
    
    @Query("SELECT tp.trainingProgramId FROM TrainingProgram tp WHERE tp.trainingProgramId LIKE 'KH-%' ORDER BY tp.trainingProgramId DESC LIMIT 1")
    String findLastTrainingProgramId();
}