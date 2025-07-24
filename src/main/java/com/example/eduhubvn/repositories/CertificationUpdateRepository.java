package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.CertificationUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationUpdateRepository extends JpaRepository<CertificationUpdate,Integer> {
    Optional<CertificationUpdate> findByCertification(Certification certification);

    List<CertificationUpdate> findByStatus(PendingStatus pendingStatus);
}
