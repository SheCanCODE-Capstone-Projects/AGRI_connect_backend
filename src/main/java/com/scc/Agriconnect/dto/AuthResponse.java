package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@Builder
@Schema(description = "Response containing authentication token after successful login or registration")
public class AuthResponse {
    
    @Schema(description = "JWT access token to be used in Authorization header for authenticated requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    
    @Schema(description = "Type of token (always 'Bearer')", example = "Bearer")
    private String tokenType;
}