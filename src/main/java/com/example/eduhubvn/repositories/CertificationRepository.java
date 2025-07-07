package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    Optional<Certification> findByIdAndStatus(Integer certificationId, PendingStatus pendingStatus);
}
