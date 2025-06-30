package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.AttendedTrainingCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnedTrainingCourseRepository extends JpaRepository<AttendedTrainingCourse, Integer> {
}
