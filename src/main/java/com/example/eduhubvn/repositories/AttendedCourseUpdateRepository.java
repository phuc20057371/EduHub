package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.AttendedTrainingCourseUpdate;
import com.example.eduhubvn.enums.PendingStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendedCourseUpdateRepository extends JpaRepository<AttendedTrainingCourseUpdate, UUID> {
    Optional<AttendedTrainingCourseUpdate> findByAttendedTrainingCourse(AttendedTrainingCourse course);

    List<AttendedTrainingCourseUpdate> findByStatus(PendingStatus pendingStatus);
}
