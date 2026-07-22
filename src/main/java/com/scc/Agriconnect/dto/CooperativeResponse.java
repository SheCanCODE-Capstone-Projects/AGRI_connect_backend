package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
@Schema(description = "Response containing cooperative information")
public class CooperativeResponse {
    
    @Schema(description = "Unique identifier of the cooperative", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID cooperativeId;
    
    @Schema(description = "Name of the cooperative", example = "Kigali Agricultural Cooperative")
    private String name;
    
    @Schema(description = "Official government registration number", example = "COOP/2024/001")
    private String registrationNumber;
    
    @Schema(description = "Province of the cooperative", example = "Kigali City")
    private String province;
    
    @Schema(description = "District of the cooperative", example = "Gasabo")
    private String district;
    
    @Schema(description = "Sector of the cooperative", example = "Remera")
    private String sector;
    
    @Schema(description = "Approval status", example = "PENDING", allowableValues = {"PENDING", "APPROVED", "REJECTED"})
    private String status;
}