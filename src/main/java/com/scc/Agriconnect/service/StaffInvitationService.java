package com.scc.Agriconnect.service;

import com.scc.Agriconnect.dto.*;
import com.scc.Agriconnect.entity.*;
import com.scc.Agriconnect.integration.EmailService;
import com.scc.Agriconnect.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StaffInvitationService {

    private final StaffInvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public StaffInvitation invite(InviteStaffRequest request) {
        User currentUser = getCurrentUser();
        Cooperative cooperative = currentUser.getCooperative();

        if (cooperative == null) {
            throw new IllegalStateException("Only a cooperative admin can invite staff");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("A user with this email already exists");
        }
        if (invitationRepository.existsByEmailAndCooperative_CooperativeIdAndStatus(
                request.getEmail(), cooperative.getCooperativeId(), StaffInvitation.InvitationStatus.PENDING)) {
            throw new IllegalArgumentException("This email already has a pending invitation");
        }

        RoleType role;
        try {
            role = RoleType.valueOf(request.getRoleName());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown role: " + request.getRoleName());
        }

        // Optional but recommended: block inviting someone as PRESIDENT or SYSTEM_ADMIN
        // through the staff-invite flow — those roles are created through registration/bootstrap only.
        if (role == RoleType.PRESIDENT || role == RoleType.SYSTEM_ADMIN) {
            throw new IllegalArgumentException("Cannot invite staff with role: " + role);
        }

        StaffInvitation invitation = invitationRepository.save(StaffInvitation.builder()
                .cooperative(cooperative)
                .email(request.getEmail())
                .role(role)
                .invitedBy(currentUser)
                .build());

        emailService.sendStaffInvitation(
                invitation.getEmail(), cooperative.getName(), role.name(), invitation.getToken());

        return invitation;
    }

    @Transactional
    public User acceptInvitation(AcceptInvitationRequest request) {
        StaffInvitation invitation = invitationRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired invitation link"));

        if (invitation.getStatus() != StaffInvitation.InvitationStatus.PENDING) {
            throw new IllegalStateException("This invitation has already been used or has expired");
        }

        User newUser = userRepository.save(User.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(invitation.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(invitation.getRole())
                .cooperative(invitation.getCooperative())
                .status(User.UserStatus.ACTIVE)
                .build());

        invitation.setStatus(StaffInvitation.InvitationStatus.ACCEPTED);
        invitation.setAcceptedAt(LocalDateTime.now());
        invitationRepository.save(invitation);

        return newUser;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}