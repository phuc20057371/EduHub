package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.EducationInstitution;
import com.example.eduhubvn.entities.EducationInstitutionUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationInstitutionUpdateRepository extends JpaRepository<EducationInstitutionUpdate, Integer> {
    Optional<EducationInstitutionUpdate> findByEducationInstitutionAndStatus(EducationInstitution institution, PendingStatus pendingStatus);
    List<EducationInstitutionUpdate> findByStatus(PendingStatus status);

    Optional<EducationInstitutionUpdate> findByIdAndStatus(Integer id, PendingStatus pendingStatus);

    Optional<EducationInstitutionUpdate> findByEducationInstitution(EducationInstitution institution);
}
