package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.StaffInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StaffInvitationRepository extends JpaRepository<StaffInvitation, Long> {
    Optional<StaffInvitation> findByToken(String token);
    boolean existsByEmailAndCooperative_CooperativeIdAndStatus(
            String email, Long cooperativeId, StaffInvitation.InvitationStatus status);
}