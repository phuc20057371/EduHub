package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.DegreeUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DegreeUpdateRepository extends JpaRepository<DegreeUpdate, Integer> {
    Optional<DegreeUpdate> findByDegree(Degree degree);
}
