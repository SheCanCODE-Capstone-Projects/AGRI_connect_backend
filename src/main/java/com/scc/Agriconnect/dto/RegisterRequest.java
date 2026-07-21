package com.scc.Agriconnect.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@PasswordMatches
public class RegisterRequest implements PasswordConfirmable {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email (message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must at least be 8 characters")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    @NotBlank(message = "Cooperative name is required")
    private String cooperativeName;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Province is required")
    private String province;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Sector is required")
    private String sector;

    @NotBlank(message = "Contact info is required")
    private String contactInfo;

    private String description;
}