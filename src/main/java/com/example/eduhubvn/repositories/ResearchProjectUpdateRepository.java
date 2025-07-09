package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.ResearchProject;
import com.example.eduhubvn.entities.ResearchProjectUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResearchProjectUpdateRepository extends JpaRepository<ResearchProjectUpdate, Integer> {
    Optional<ResearchProjectUpdate> findByResearchProject(ResearchProject project);
}
