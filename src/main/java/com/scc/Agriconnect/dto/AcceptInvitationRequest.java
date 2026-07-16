package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.dto.PasswordConfirmable;
import com.scc.Agriconnect.dto.PasswordMatches;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@PasswordMatches
public class AcceptInvitationRequest implements PasswordConfirmable {

    @NotBlank private String token;
    @NotBlank private String fullName;
    @NotBlank private String phoneNumber;
    @NotBlank @Size(min = 8) private String password;
    @NotBlank private String confirmPassword;
}