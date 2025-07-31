package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.CourseLecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseLecturerRepository extends JpaRepository<CourseLecturer, UUID> {
}
