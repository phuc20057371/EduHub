package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PendingEducationInstitution;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingEducationInstitutionRepository extends JpaRepository<PendingEducationInstitution, Integer> {
    List<PendingEducationInstitution> findByStatus(PendingStatus status);

}
