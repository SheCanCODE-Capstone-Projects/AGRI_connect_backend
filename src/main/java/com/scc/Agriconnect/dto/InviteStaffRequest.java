package com.scc.Agriconnect.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class InviteStaffRequest {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String roleName;
}