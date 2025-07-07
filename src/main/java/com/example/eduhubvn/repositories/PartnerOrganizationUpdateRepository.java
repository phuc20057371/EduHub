package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PartnerOrganizationUpdate;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartnerOrganizationUpdateRepository extends JpaRepository<PartnerOrganizationUpdate, Integer> {
    Optional<PartnerOrganizationUpdate> findByPartnerOrganizationAndStatus(PartnerOrganization partnerOrganization, PendingStatus pendingStatus);
    List<PartnerOrganizationUpdate> findByStatus(PendingStatus status);

    Optional<PartnerOrganizationUpdate> findByIdAndStatus(Integer id, PendingStatus pendingStatus);

    Optional<PartnerOrganizationUpdate> findByPartnerOrganization(PartnerOrganization partnerOrganization);
}
