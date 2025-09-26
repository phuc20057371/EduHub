package com.example.eduhubvn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eduhubvn.entities.Application;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    
}
