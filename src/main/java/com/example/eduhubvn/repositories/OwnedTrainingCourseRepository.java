package com.example.eduhubvn.repositories;


import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.OwnedTrainingCourse;
import com.example.eduhubvn.entities.OwnedTrainingCourseUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OwnedTrainingCourseRepository extends JpaRepository<OwnedTrainingCourse, Integer> {


    List<OwnedTrainingCourse> findByLecturer(Lecturer lecturer);
}
