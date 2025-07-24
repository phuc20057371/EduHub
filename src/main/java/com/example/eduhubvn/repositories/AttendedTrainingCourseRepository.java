package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.AttendedTrainingCourse;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface AttendedTrainingCourseRepository extends JpaRepository<AttendedTrainingCourse, Integer> {
    List<AttendedTrainingCourse> findByLecturer(Lecturer lecturer);

    List<AttendedTrainingCourse> findByStatus(PendingStatus pendingStatus);

    @Query("SELECT d FROM AttendedTrainingCourse d JOIN FETCH d.lecturer l WHERE d.status = :status AND l.status = 'APPROVED'")
    List<AttendedTrainingCourse> findByStatusWithApprovedLecturer(@Param("status")  PendingStatus pendingStatus);
}
