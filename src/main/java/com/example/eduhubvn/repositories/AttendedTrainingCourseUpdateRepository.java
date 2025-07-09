package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.AttendedTrainingCourseUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendedTrainingCourseUpdateRepository extends JpaRepository<AttendedTrainingCourseUpdate, Integer> {
    Optional<AttendedTrainingCourseUpdate> findByAttendedTrainingCourse(AttendedTrainingCourse course);
}
