package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Course;
import com.example.eduhubvn.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    
    @Query("SELECT c FROM Course c JOIN c.courseLecturers cl WHERE cl.lecturer = :lecturer")
    List<Course> findCoursesByLecturer(@Param("lecturer") Lecturer lecturer);
}
