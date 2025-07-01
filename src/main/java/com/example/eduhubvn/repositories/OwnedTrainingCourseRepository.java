package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.OwnedTrainingCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnedTrainingCourseRepository extends JpaRepository<OwnedTrainingCourse, Integer> {
}
