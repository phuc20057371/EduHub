package com.example.eduhubvn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eduhubvn.entities.TrainingUnit;

@Repository
public interface TrainingUnitRepository extends JpaRepository<TrainingUnit, UUID> {
    
}