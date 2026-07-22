package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter @Builder
@Schema(description = "Response containing member information")
public class MemberResponse {
    
    @Schema(description = "Unique identifier of the member", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID memberId;
    
    @Schema(description = "Full name of the member", example = "Jean Baptiste Mukiza")
    private String fullName;
    
    @Schema(description = "Phone number of the member", example = "+250788456789")
    private String phoneNumber;
    
    @Schema(description = "National ID number", example = "1199880012345678")
    private String nationalId;
    
    @Schema(description = "Physical address", example = "Kigali, Gasabo, Remera")
    private String address;
    
    @Schema(description = "Gender", example = "Male")
    private String gender;
    
    @Schema(description = "Date when member joined", example = "2024-01-15")
    private LocalDate dateJoined;
    
    @Schema(description = "Current membership status", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
    private String membershipStatus;
}