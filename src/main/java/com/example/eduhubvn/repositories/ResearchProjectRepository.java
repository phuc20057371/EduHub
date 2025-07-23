package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.ResearchProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResearchProjectRepository extends JpaRepository<ResearchProject, Integer> {
    List<ResearchProject> findByLecturer(Lecturer lecturer);
}
