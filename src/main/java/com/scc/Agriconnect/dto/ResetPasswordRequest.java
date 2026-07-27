package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@PasswordMatches
@Schema(description = "Request body for completing password reset using reset token")
public class ResetPasswordRequest implements PasswordConfirmable {

    @Schema(description = "Password reset token received via email", example = "d9b2b3a4-5c6d-7e8f-9a0b-1c2d3e4f5a6b")
    @NotBlank(message = "Token is required")
    private String token;

    @Schema(description = "New password (minimum 6 characters)", example = "NewSecurePassword123")
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(description = "Confirmation of new password", example = "NewSecurePassword123")
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
