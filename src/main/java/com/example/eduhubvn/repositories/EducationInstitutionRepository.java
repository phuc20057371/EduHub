package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface EducationInstitutionRepository extends JpaRepository<EducationInstitution, UUID> {
    List<EducationInstitution> findByStatus(PendingStatus status);
}
