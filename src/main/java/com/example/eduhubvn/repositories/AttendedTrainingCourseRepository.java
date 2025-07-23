package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendedTrainingCourseRepository extends JpaRepository<AttendedTrainingCourse, Integer> {
    List<AttendedTrainingCourse> findByLecturer(Lecturer lecturer);
}
