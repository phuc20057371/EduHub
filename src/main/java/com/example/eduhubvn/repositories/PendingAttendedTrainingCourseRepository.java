package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PendingAttendedTrainingCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingAttendedTrainingCourseRepository extends JpaRepository<PendingAttendedTrainingCourse, Integer> {
}
