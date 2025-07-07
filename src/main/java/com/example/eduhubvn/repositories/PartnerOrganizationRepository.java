package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PartnerOrganizationRepository extends JpaRepository<PartnerOrganization, Integer> {
    List<PartnerOrganization> findByStatus(PendingStatus status);
}
