package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.DegreeUpdate;
import com.example.eduhubvn.enums.PendingStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DegreeUpdateRepository extends JpaRepository<DegreeUpdate, UUID> {
    Optional<DegreeUpdate> findByDegree(Degree degree);

    List<DegreeUpdate> findByStatus(PendingStatus pendingStatus);

    @Query("SELECT du FROM DegreeUpdate du JOIN du.degree d JOIN d.lecturer l " +
            "WHERE du.status = :status AND l.hidden = false")
    List<DegreeUpdate> findByStatusWithVisibleLecturer(@Param("status") PendingStatus status);
}
