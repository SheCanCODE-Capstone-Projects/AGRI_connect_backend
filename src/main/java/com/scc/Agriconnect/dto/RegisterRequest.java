package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.dto.PasswordConfirmable;
import com.scc.Agriconnect.dto.PasswordMatches;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@PasswordMatches
public class RegisterRequest implements PasswordConfirmable {

    @NotBlank private String fullName;
    @NotBlank private String phoneNumber;
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 8) private String password;
    @NotBlank private String confirmPassword;

    @NotBlank private String cooperativeName;
    @NotBlank private String registrationNumber;
    @NotBlank private String province;
    @NotBlank private String district;
    @NotBlank private String sector;
    @NotBlank private String contactInfo;
    private String description;
}