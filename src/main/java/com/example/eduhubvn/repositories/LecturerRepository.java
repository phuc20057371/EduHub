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
import com.example.eduhubvn.enums.PendingStatus;

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


    @Query(value = "SELECT * FROM lecturer WHERE lecturer_id IS NOT NULL ORDER BY lecturer_id DESC LIMIT 1", nativeQuery = true)
    Optional<Lecturer> findTopByOrderByLecturerIdDesc();

    @Query("SELECT AVG(tp.rating) FROM TrainingProgram tp JOIN tp.units tu WHERE tu.lecturer.id = :lecturerId AND tp.rating IS NOT NULL")
    Double calculateAverageRatingForLecturer(@Param("lecturerId") UUID lecturerId);

    @Query("""
        SELECT l FROM Lecturer l 
        WHERE l.hidden = false AND l.jobField IS NOT NULL
        AND EXISTS (
            SELECT 1 FROM TrainingUnit tu JOIN tu.trainingProgram tp 
            WHERE tu.lecturer.id = l.id AND tp.rating IS NOT NULL
        )
        """)
    List<Lecturer> findLecturersWithRating();

    // Pagination support
    Page<Lecturer> findAll(Pageable pageable);

}
