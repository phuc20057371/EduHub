package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PendingLecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PendingLecturerRepository extends JpaRepository<PendingLecturer, Integer> {

    Optional<PendingLecturer> findByUserEmail(String email);
}
