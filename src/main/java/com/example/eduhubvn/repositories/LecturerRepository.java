package com.example.eduhubvn.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingStatus;

public interface LecturerRepository extends JpaRepository<Lecturer, UUID> {
    List<Lecturer> findByStatus(PendingStatus status);

    @Query("SELECT l FROM Lecturer l JOIN FETCH l.user WHERE l.id = :id")
    Optional<Lecturer> findByIdWithUser(@Param("id") UUID id);

    @Query("SELECT l FROM Lecturer l JOIN FETCH l.user WHERE l.id IN :ids")
    List<Lecturer> findAllByIdWithUser(@Param("ids") Collection<UUID> ids);

    Optional<Lecturer> findByCitizenId(String citizenId);

    boolean existsByCitizenId(String citizenId);

    boolean existsByCitizenIdAndIdNot(String citizenId, UUID id);

    @Query("SELECT l FROM Lecturer l JOIN FETCH l.user WHERE l.id IN :ids AND l.hidden = false")
    List<Lecturer> findAllByIdWithUser(@Param("ids") Set<UUID> ids);

    List<Lecturer> findTop7ByHiddenFalseOrderByExperienceYearsDesc();

    @Query(value = "SELECT * FROM lecturer WHERE lecturer_id IS NOT NULL ORDER BY lecturer_id DESC LIMIT 1", nativeQuery = true)
    Optional<Lecturer> findTopByOrderByLecturerIdDesc();

    // Pagination support
    Page<Lecturer> findAll(Pageable pageable);

}
