package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.LecturerUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LecturerUpdateRepository extends JpaRepository<LecturerUpdate, Integer> {
    Optional<LecturerUpdate> findByLecturerAndStatus(Lecturer lecturer, PendingStatus pendingStatus);
    List<LecturerUpdate> findByStatus(PendingStatus status);

    Optional<LecturerUpdate> findByIdAndStatus(Integer updateId, PendingStatus pendingStatus);

    Optional<LecturerUpdate> findByLecturer(Lecturer lecturer);
}
