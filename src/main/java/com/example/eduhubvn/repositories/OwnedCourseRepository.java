package com.example.eduhubvn.repositories;


import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.OwnedTrainingCourse;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OwnedCourseRepository extends JpaRepository<OwnedTrainingCourse, UUID> {


    List<OwnedTrainingCourse> findByLecturer(Lecturer lecturer);

    List<OwnedTrainingCourse> findByStatus(PendingStatus pendingStatus);

    // In OwnedTrainingCourseRepository
    @Query("SELECT o FROM OwnedTrainingCourse o JOIN FETCH o.lecturer l WHERE o.status = :status AND l.status = 'APPROVED'")
    List<OwnedTrainingCourse> findByStatusWithApprovedLecturer(@Param("status") PendingStatus status);

    List<OwnedTrainingCourse> findByLecturerIdAndStatus(UUID id, PendingStatus pending);
}
