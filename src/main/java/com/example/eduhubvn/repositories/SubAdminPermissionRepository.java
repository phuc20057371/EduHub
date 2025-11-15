package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.SubAdminPermission;
import com.example.eduhubvn.entities.User;
import com.example.eduhubvn.enums.Permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubAdminPermissionRepository extends JpaRepository<SubAdminPermission, UUID> {
    
    List<SubAdminPermission> findByUser(User user);
    
    List<SubAdminPermission> findByUserId(UUID userId);
    
    @Query("SELECT sap FROM SubAdminPermission sap WHERE sap.user.id = :userId AND sap.permission = :permission")
    SubAdminPermission findByUserIdAndPermission(@Param("userId") UUID userId, @Param("permission") Permission permission);
    
    void deleteByUserAndPermission(User user, Permission permission);
    
    void deleteByUserId(UUID userId);
    
    @Query("SELECT DISTINCT sap.permission FROM SubAdminPermission sap WHERE sap.user.id = :userId")
    List<Permission> findPermissionsByUserId(@Param("userId") UUID userId);
}
