package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.SmsRequest;
import com.scc.Agriconnect.dto.SmsResponse;
import com.scc.Agriconnect.service.SmsService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cooperative/sms")
@RequiredArgsConstructor
@Tag(name = "SMS Communication", description = "Send SMS announcements to cooperative members and track delivery. Requires PRESIDENT role to send.")
@SecurityRequirement(name = "bearerAuth")
public class SmsController {

    private final SmsService smsService;

    @Operation(
        summary = "Send an SMS announcement",
        description = "Send an SMS to all active members, or to a specific list of members. Requires PRESIDENT role."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "SMS sent and delivery outcomes recorded",
            content = @Content(schema = @Schema(implementation = SmsResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data or no active members to send to"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasRole('PRESIDENT')")
    @PostMapping
    public ResponseEntity<SmsResponse> send(@Valid @RequestBody SmsRequest request) {
        return ResponseEntity.ok(smsService.send(request));
    }

    @Operation(
        summary = "List SMS announcement history",
        description = "Get paginated SMS announcements sent by the authenticated user's cooperative"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SMS history retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<Page<SmsResponse>> list(
            @Parameter(description = "Pagination parameters (page, size, sort)")
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(smsService.getCooperativeSmsHistory(pageable));
    }

    @Operation(
        summary = "Get SMS announcement by ID",
        description = "Retrieve a specific SMS announcement, including per-member delivery outcomes"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "SMS found",
            content = @Content(schema = @Schema(implementation = SmsResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "SMS not found")
    })
    @GetMapping("/{smsId}")
    public ResponseEntity<SmsResponse> get(
            @Parameter(description = "SMS UUID")
            @PathVariable UUID smsId) {
        return ResponseEntity.ok(smsService.getSmsById(smsId));
    }
}
