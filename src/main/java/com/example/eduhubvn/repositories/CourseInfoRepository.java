package com.example.eduhubvn.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eduhubvn.entities.CourseInfo;

public interface CourseInfoRepository extends JpaRepository<CourseInfo, UUID> {
    
}
