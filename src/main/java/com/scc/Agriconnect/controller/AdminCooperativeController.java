package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.CooperativeResponse;
import com.scc.Agriconnect.mapper.CooperativeMapper;
import com.scc.Agriconnect.service.AdminCooperativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/cooperatives")
@RequiredArgsConstructor
@Tag(name = "Admin – Cooperative Management", description = "System administration endpoints for managing cooperative registrations. Requires SYSTEM_ADMIN role. Admins review, approve, or reject pending cooperative applications.")
@SecurityRequirement(name = "bearerAuth")
public class AdminCooperativeController {

    private final AdminCooperativeService adminCooperativeService;

    @Operation(
        summary = "List pending cooperatives",
        description = "Get all cooperatives awaiting approval. New cooperatives register with PENDING status and cannot log in until approved."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of pending cooperatives retrieved successfully"
        ),
        @ApiResponse(responseCode = "403", description = "Access denied — SYSTEM_ADMIN role required")
    })
    @GetMapping("/pending")
    public ResponseEntity<List<CooperativeResponse>> getPending() {
        var response = adminCooperativeService.getPending().stream()
                .map(CooperativeMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Approve a cooperative",
        description = "Approve a pending cooperative registration. Allows the cooperative president to log in and start using the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cooperative approved successfully",
            content = @Content(schema = @Schema(implementation = CooperativeResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Cooperative not found"),
        @ApiResponse(responseCode = "400", description = "Cooperative is not in PENDING status"),
        @ApiResponse(responseCode = "403", description = "Access denied — SYSTEM_ADMIN role required")
    })
    @PatchMapping("/{id}/approve")
    public ResponseEntity<CooperativeResponse> approve(
            @Parameter(description = "Cooperative UUID") 
            @PathVariable UUID id) {
        return ResponseEntity.ok(CooperativeMapper.toResponse(adminCooperativeService.approve(id)));
    }

    @Operation(
        summary = "Reject a cooperative",
        description = "Reject a pending cooperative registration. The cooperative president will not be able to access the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cooperative rejected successfully",
            content = @Content(schema = @Schema(implementation = CooperativeResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Cooperative not found"),
        @ApiResponse(responseCode = "400", description = "Cooperative is not in PENDING status"),
        @ApiResponse(responseCode = "403", description = "Access denied — SYSTEM_ADMIN role required")
    })
    @PatchMapping("/{id}/reject")
    public ResponseEntity<CooperativeResponse> reject(
            @Parameter(description = "Cooperative UUID") 
            @PathVariable UUID id) {
        return ResponseEntity.ok(CooperativeMapper.toResponse(adminCooperativeService.reject(id)));
    }
}