package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.dto.PasswordConfirmable;
import com.scc.Agriconnect.dto.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@PasswordMatches
@Schema(description = "Request payload for completing registration after accepting a staff invitation")
public class AcceptInvitationRequest implements PasswordConfirmable {

    @Schema(description = "Invitation token received via email", example = "eyJhbGciOiJIUzI1NiJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank private String token;
    
    @Schema(description = "Full name of the staff member", example = "Alice Ineza", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank private String fullName;
    
    @Schema(description = "Phone number of the staff member", example = "+250788111222", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank private String phoneNumber;
    
    @Schema(description = "Password for the new account (minimum 8 characters)", example = "StaffPass123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Size(min = 8) private String password;
    
    @Schema(description = "Password confirmation (must match password)", example = "StaffPass123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank private String confirmPassword;
}