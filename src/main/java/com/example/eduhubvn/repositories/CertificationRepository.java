package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    Optional<Certification> findByIdAndStatus(Integer certificationId, PendingStatus pendingStatus);

    List<Certification> findByLecturerAndStatus(Lecturer lecturer, PendingStatus pendingStatus);

    List<Certification> findByLecturer(Lecturer lecturer);

    List<Certification> findByStatus(PendingStatus pendingStatus);

    @Query("SELECT d FROM Certification d JOIN FETCH d.lecturer l WHERE d.status = :status AND l.status = 'APPROVED'")
    List<Certification> findByStatusWithApprovedLecturer(@Param("status") PendingStatus pendingStatus);
}
