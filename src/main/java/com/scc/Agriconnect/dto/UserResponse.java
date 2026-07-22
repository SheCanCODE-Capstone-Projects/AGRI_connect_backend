package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
@Schema(description = "Response containing user account information")
public class UserResponse {
    
    @Schema(description = "Unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;
    
    @Schema(description = "Full name of the user", example = "Alice Ineza")
    private String fullName;
    
    @Schema(description = "Email address (used for login)", example = "alice@example.com")
    private String email;
    
    @Schema(description = "Role assigned to the user", example = "STAFF", allowableValues = {"SYSTEM_ADMIN", "PRESIDENT", "ACCOUNTANT", "STAFF"})
    private String role;
    
    @Schema(description = "UUID of the cooperative the user belongs to", example = "456e7890-e12b-34d5-a678-901234567890")
    private UUID cooperativeId;
}