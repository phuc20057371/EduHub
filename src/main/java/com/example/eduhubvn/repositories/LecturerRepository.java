package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    List<Lecturer> findByStatus(PendingStatus status);
}
