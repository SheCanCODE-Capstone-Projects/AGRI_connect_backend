package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@PasswordMatches
public class RegisterRequest implements PasswordConfirmable {

    @Schema(description = "Full name of the cooperative president", example = "John Doe Uwase")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Schema(description = "Phone number of the president", example = "+250788123456")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Schema(description = "Email address for the president account (used for login)", example = "president@example.com")
    @NotBlank(message = "Email is required")
    @Email (message = "Email must be valid")
    private String email;

    @Schema(description = "Password for the account (minimum 8 characters)", example = "SecurePass123")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must at least be 8 characters")
    private String password;

    @Schema(description = "Password confirmation (must match password)", example = "SecurePass123")
    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    @Schema(description = "Official name of the cooperative", example = "Kigali Agricultural Cooperative")
    @NotBlank(message = "Cooperative name is required")
    private String cooperativeName;

    @Schema(description = "Government registration number for the cooperative", example = "COOP/2024/001")
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @Schema(description = "Province where the cooperative is located", example = "Kigali City")
    @NotBlank(message = "Province is required")
    private String province;

    @Schema(description = "District where the cooperative is located", example = "Gasabo")
    @NotBlank(message = "District is required")
    private String district;

    @Schema(description = "Sector where the cooperative is located", example = "Remera")
    @NotBlank(message = "Sector is required")
    private String sector;

    @Schema(description = "Contact information (phone, email, or address)", example = "+250788000000")
    @NotBlank(message = "Contact info is required")
    private String contactInfo;

    @Schema(description = "Optional description of the cooperative's activities", example = "A cooperative focused on sustainable farming practices")
    private String description;
}