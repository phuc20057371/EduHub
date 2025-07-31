package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PartnerOrganizationUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartnerOrganizationUpdateRepository extends JpaRepository<PartnerOrganizationUpdate, UUID> {
    Optional<PartnerOrganizationUpdate> findByPartnerOrganizationAndStatus(PartnerOrganization partnerOrganization, PendingStatus pendingStatus);
    List<PartnerOrganizationUpdate> findByStatus(PendingStatus status);

    Optional<PartnerOrganizationUpdate> findByPartnerOrganization(PartnerOrganization partnerOrganization);
}
