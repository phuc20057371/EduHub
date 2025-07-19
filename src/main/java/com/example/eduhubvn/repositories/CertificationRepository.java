package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    Optional<Certification> findByIdAndStatus(Integer certificationId, PendingStatus pendingStatus);

    List<Certification> findByLecturerAndStatus(Lecturer lecturer, PendingStatus pendingStatus);

    List<Certification> findByLecturer(Lecturer lecturer);
}
