package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Request payload for user authentication")
public class LoginRequest {

    @Schema(description = "Email address of the user", example = "president@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Email
    private String email;

    @Schema(description = "User password", example = "SecurePass123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;
}