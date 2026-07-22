package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request payload for creating or updating a customer")
public class CustomerRequest {

    @Schema(description = "Full name of the customer", example = "Marie Claire Uwera", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Schema(description = "Phone number of the customer", example = "+250788987654", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Schema(description = "Physical address of the customer", example = "Kigali, Kicukiro")
    private String address;
}