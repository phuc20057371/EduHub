package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Degree;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DegreeRepository extends JpaRepository<Degree, Integer> {
    Optional<Degree> findByIdAndStatus(Integer id, PendingStatus pendingStatus);

    List<Degree> findByLecturerAndStatus(Lecturer lecturer, PendingStatus pendingStatus);

    List<Degree> findByStatus(PendingStatus pendingStatus);

    List<Degree> findByLecturer(Lecturer lecturer);

    @Query("SELECT d FROM Degree d JOIN FETCH d.lecturer l WHERE d.status = :status AND l.status = 'APPROVED'")
    List<Degree> findByStatusWithApprovedLecturer(@Param("status") PendingStatus pendingStatus);
}
