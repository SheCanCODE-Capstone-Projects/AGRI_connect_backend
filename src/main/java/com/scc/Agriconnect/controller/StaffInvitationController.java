package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.*;
import com.scc.Agriconnect.mapper.InvitationMapper;
import com.scc.Agriconnect.mapper.UserMapper;
import com.scc.Agriconnect.service.StaffInvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StaffInvitationController {

    private final StaffInvitationService staffInvitationService;

    @PreAuthorize("hasRole('PRESIDENT')")
    @PostMapping("/api/cooperative/staff/invite")
    public ResponseEntity<InvitationResponse> invite(@Valid @RequestBody InviteStaffRequest request) {
        return ResponseEntity.ok(InvitationMapper.toResponse(staffInvitationService.invite(request)));
    }

    @PostMapping("/api/auth/accept-invitation")
    public ResponseEntity<UserResponse> acceptInvitation(@Valid @RequestBody AcceptInvitationRequest request) {
        return ResponseEntity.ok(UserMapper.toResponse(staffInvitationService.acceptInvitation(request)));
    }
}