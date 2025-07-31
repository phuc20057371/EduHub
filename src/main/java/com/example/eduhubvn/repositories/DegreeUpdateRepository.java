package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.DegreeUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DegreeUpdateRepository extends JpaRepository<DegreeUpdate, UUID> {
    Optional<DegreeUpdate> findByDegree(Degree degree);

    List<DegreeUpdate> findByStatus(PendingStatus pendingStatus);
}
