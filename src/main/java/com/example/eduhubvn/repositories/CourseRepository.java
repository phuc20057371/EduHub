package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
