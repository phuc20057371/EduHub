package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.OwnedTrainingCourse;
import com.example.eduhubvn.entities.OwnedTrainingCourseUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OwnedTrainingCourseUpdateRepository extends JpaRepository<OwnedTrainingCourseUpdate, UUID> {
    Optional<OwnedTrainingCourseUpdate> findByOwnedTrainingCourse(OwnedTrainingCourse course);

    List<OwnedTrainingCourseUpdate> findByStatus(PendingStatus pendingStatus);
}
