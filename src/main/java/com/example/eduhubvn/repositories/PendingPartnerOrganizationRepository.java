package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PendingPartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingPartnerOrganizationRepository extends JpaRepository<PendingPartnerOrganization, Integer> {
    List<PendingPartnerOrganization> findByStatus(PendingStatus status);
}
