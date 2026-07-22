package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.*;
import com.scc.Agriconnect.mapper.InvitationMapper;
import com.scc.Agriconnect.mapper.UserMapper;
import com.scc.Agriconnect.service.StaffInvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Staff Invitation", description = "Invite staff members to join a cooperative and handle invitation acceptance. Presidents can invite staff with specific roles (STAFF, ACCOUNTANT, STOCKMANAGER).")
public class StaffInvitationController {

    private final StaffInvitationService staffInvitationService;

    @Operation(
        summary = "Invite staff member",
        description = "Send an invitation email to a staff member to join the cooperative with a specified role. Only PRESIDENT can invite staff. Valid roles: STAFF, ACCOUNTANT, STOCKMANAGER"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Invitation sent successfully",
            content = @Content(schema = @Schema(implementation = InvitationResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid email or role"),
        @ApiResponse(responseCode = "403", description = "Only PRESIDENT can invite staff"),
        @ApiResponse(responseCode = "409", description = "Email already registered or has pending invitation")
    })
    @PreAuthorize("hasRole('PRESIDENT')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/api/cooperative/staff/invite")
    public ResponseEntity<InvitationResponse> invite(@Valid @RequestBody InviteStaffRequest request) {
        return ResponseEntity.ok(InvitationMapper.toResponse(staffInvitationService.invite(request)));
    }

    @Operation(
        summary = "Accept staff invitation",
        description = "Complete registration by accepting a staff invitation. Requires a valid invitation token received via email. Creates a user account with the specified role."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Invitation accepted and user account created",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
        @ApiResponse(responseCode = "404", description = "Invitation not found")
    })
    @PostMapping("/api/auth/accept-invitation")
    public ResponseEntity<UserResponse> acceptInvitation(@Valid @RequestBody AcceptInvitationRequest request) {
        return ResponseEntity.ok(UserMapper.toResponse(staffInvitationService.acceptInvitation(request)));
    }
}