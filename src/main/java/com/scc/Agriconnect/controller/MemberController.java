package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.MemberRequest;
import com.scc.Agriconnect.dto.MemberResponse;
import com.scc.Agriconnect.entity.Member;
import com.scc.Agriconnect.mapper.MemberMapper;
import com.scc.Agriconnect.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
@Tag(name = "Member Management", description = "Manage cooperative members. Includes creating, updating, listing, and deactivating/reactivating members. Requires PRESIDENT or STAFF role.")
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    private final MemberService memberService;

    @Operation(
        summary = "Create a new member",
        description = "Register a new member in the cooperative. Member will be in ACTIVE status by default."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Member created successfully",
            content = @Content(schema = @Schema(implementation = MemberResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Member with this national ID already exists")
    })
    @PostMapping
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.create(request)));
    }

    @Operation(
        summary = "List all members",
        description = "Retrieve all members for the authenticated user's cooperative. Can filter by membership status and name."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of members retrieved successfully"
        ),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<List<MemberResponse>> list(
            @Parameter(description = "Filter by membership status (ACTIVE, INACTIVE)") 
            @RequestParam(required = false) Member.MembershipStatus status,
            @Parameter(description = "Filter by member name (partial match)")
            @RequestParam(required = false) String name) {

        List<MemberResponse> response = memberService.list(status, name).stream()
                .map(MemberMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get member by ID",
        description = "Retrieve detailed information about a specific member by their ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Member found",
            content = @Content(schema = @Schema(implementation = MemberResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getOne(
            @Parameter(description = "Member UUID") 
            @PathVariable UUID id) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.getOne(id)));
    }

    @Operation(
        summary = "Update member information",
        description = "Update an existing member's details. Cannot change membership status through this endpoint."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Member updated successfully",
            content = @Content(schema = @Schema(implementation = MemberResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> update(
            @Parameter(description = "Member UUID") 
            @PathVariable UUID id, 
            @Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.update(id, request)));
    }

    @Operation(
        summary = "Deactivate a member",
        description = "Soft delete a member by setting their status to INACTIVE. Member data is preserved."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Member deactivated successfully",
            content = @Content(schema = @Schema(implementation = MemberResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> softDelete(
            @Parameter(description = "Member UUID") 
            @PathVariable UUID id) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.softDelete(id)));
    }

    @Operation(
        summary = "Reactivate a member",
        description = "Restore an inactive member back to ACTIVE status"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Member reactivated successfully",
            content = @Content(schema = @Schema(implementation = MemberResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "400", description = "Member is already active")
    })
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<MemberResponse> reactivate(
            @Parameter(description = "Member UUID") 
            @PathVariable UUID id) {
        return ResponseEntity.ok(MemberMapper.toResponse(memberService.reactivate(id)));
    }
}