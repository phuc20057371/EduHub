package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PendingOwnedTrainingCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PendingOwnedTrainingCourseRepository extends JpaRepository<PendingOwnedTrainingCourse, Integer> {
    Collection<Object> findByLecturerId(Integer lecturerId);
}
