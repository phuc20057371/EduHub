package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    List<Lecturer> findByStatus(PendingStatus status);

    @Query("SELECT l FROM Lecturer l JOIN FETCH l.user WHERE l.id = :id")
    Optional<Lecturer> findByIdWithUser(@Param("id") Integer id);
}
