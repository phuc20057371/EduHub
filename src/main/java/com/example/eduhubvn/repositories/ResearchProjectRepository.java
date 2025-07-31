package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.ResearchProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ResearchProjectRepository extends JpaRepository<ResearchProject, UUID> {
    List<ResearchProject> findByLecturer(Lecturer lecturer);

    List<ResearchProject> findByStatus(PendingStatus pendingStatus);

    @Query("SELECT o FROM ResearchProject o JOIN FETCH o.lecturer l WHERE o.status = :status AND l.status = 'APPROVED'")
    List<ResearchProject> findByStatusWithApprovedLecturer(@Param("status") PendingStatus pendingStatus);
}
