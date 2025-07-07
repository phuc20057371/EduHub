package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DegreeRepository extends JpaRepository<Degree, Integer> {
    Optional<Degree> findByIdAndStatus(Integer id, PendingStatus pendingStatus);
}
