package com.example.eduhubvn.repositories;


import com.example.eduhubvn.entities.AcademicRank;
import com.example.eduhubvn.entities.Lecturer;
import com.example.eduhubvn.entities.Role;
import com.example.eduhubvn.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    @Query("SELECT l FROM Lecturer l WHERE " +
            "(:academicRank IS NULL OR l.academicRank = :academicRank) AND " +
            "(:specialization IS NULL OR :specialization = '' OR LOWER(l.specialization) LIKE LOWER(CONCAT('%', :specialization, '%'))) AND " +
            "l.status = 'APPROVED'")
    List<Lecturer> findByAcademicRankAndSpecializationContaining(
            @Param("academicRank") AcademicRank academicRank,
            @Param("specialization") String specialization
    );

    /**
     * Tìm user bằng email chính hoặc subEmails
     * Sử dụng JPQL để tối ưu performance
     */
    @Query("SELECT u FROM User u WHERE u.email = :email OR :email MEMBER OF u.subEmails")
    Optional<User> findByEmailOrSubEmail(@Param("email") String email);

    /**
     * Tìm user có chứa email trong subEmails
     */
    @Query("SELECT u FROM User u WHERE :email MEMBER OF u.subEmails")
    Optional<User> findBySubEmail(@Param("email") String email);

}
