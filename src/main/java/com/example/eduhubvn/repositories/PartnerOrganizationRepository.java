package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PartnerOrganizationRepository extends JpaRepository<PartnerOrganization, UUID> {
    List<PartnerOrganization> findByStatus(PendingStatus status);

    boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber);
}
