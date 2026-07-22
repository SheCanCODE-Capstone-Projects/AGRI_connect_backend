package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@Schema(description = "Request payload for creating or updating a cooperative member")
public class MemberRequest {

    @Schema(description = "Full name of the member", example = "Jean Baptiste Mukiza", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String fullName;

    @Schema(description = "Phone number of the member", example = "+250788456789", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String phoneNumber;

    @Schema(description = "National ID number of the member", example = "1199880012345678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String nationalId;

    @Schema(description = "Physical address of the member", example = "Kigali, Gasabo, Remera", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String address;

    @Schema(description = "Gender of the member", example = "Male", allowableValues = {"Male", "Female", "Other"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String gender;

    @Schema(description = "Date when the member joined the cooperative (defaults to current date if not provided)", example = "2024-01-15")
    private LocalDate dateJoined;
}