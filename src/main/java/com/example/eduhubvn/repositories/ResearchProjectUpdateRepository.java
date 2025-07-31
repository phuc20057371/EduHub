package com.example.eduhubvn.repositories;

import com.example.eduhubvn.dtos.lecturer.ResearchProjectUpdateDTO;
import com.example.eduhubvn.entities.PendingStatus;
import com.example.eduhubvn.entities.ResearchProject;
import com.example.eduhubvn.entities.ResearchProjectUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResearchProjectUpdateRepository extends JpaRepository<ResearchProjectUpdate, UUID> {
    Optional<ResearchProjectUpdate> findByResearchProject(ResearchProject project);

    List<ResearchProjectUpdate> findByStatus(PendingStatus pendingStatus);
}
