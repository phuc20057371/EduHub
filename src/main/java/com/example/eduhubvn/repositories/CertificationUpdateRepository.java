package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Certification;
import com.example.eduhubvn.entities.CertificationUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificationUpdateRepository extends JpaRepository<CertificationUpdate,Integer> {
    Optional<CertificationUpdate> findByCertification(Certification certification);
}
