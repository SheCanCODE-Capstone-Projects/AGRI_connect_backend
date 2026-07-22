package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@Schema(description = "Request payload for inviting a staff member to join a cooperative")
public class InviteStaffRequest {

    @Schema(description = "Email address of the staff member to invite", example = "staff@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank @Email
    private String email;

    @Schema(description = "Role to assign to the staff member", example = "STAFF", allowableValues = {"STAFF", "ACCOUNTANT", "STOCKMANAGER"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String roleName;
}