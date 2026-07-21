package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.StaffInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface StaffInvitationRepository extends JpaRepository<StaffInvitation, UUID> {
    Optional<StaffInvitation> findByToken(String token);
    boolean existsByEmailAndCooperative_CooperativeIdAndStatus(
            String email, UUID cooperativeId, StaffInvitation.InvitationStatus status);
}