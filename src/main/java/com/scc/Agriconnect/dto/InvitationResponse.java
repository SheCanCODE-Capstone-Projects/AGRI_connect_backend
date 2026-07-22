package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
@Schema(description = "Response for a staff invitation")
public class InvitationResponse {
    
    @Schema(description = "Unique identifier of the invitation", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID invitationId;
    
    @Schema(description = "Email address the invitation was sent to", example = "staff@example.com")
    private String email;
    
    @Schema(description = "Role assigned in the invitation", example = "STAFF", allowableValues = {"STAFF", "ACCOUNTANT", "STOCKMANAGER"})
    private String role;
    
    @Schema(description = "Current status of the invitation", example = "PENDING", allowableValues = {"PENDING", "ACCEPTED", "EXPIRED"})
    private String status;
}