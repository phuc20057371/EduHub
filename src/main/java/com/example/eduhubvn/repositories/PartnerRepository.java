package com.example.eduhubvn.repositories;

import com.example.eduhubvn.entities.PartnerOrganization;
import com.example.eduhubvn.entities.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PartnerRepository extends JpaRepository<PartnerOrganization, UUID> {
    List<PartnerOrganization> findByStatus(PendingStatus status);

    boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber);

    boolean existsByBusinessRegistrationNumberAndIdNot(String businessRegistrationNumber, UUID id);
}
