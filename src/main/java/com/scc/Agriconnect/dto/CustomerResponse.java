package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Response containing customer information")
public class CustomerResponse {
    
    @Schema(description = "Unique identifier of the customer", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID customerId;
    
    @Schema(description = "Cooperative ID that owns this customer record", example = "456e7890-e12b-34d5-a678-901234567890")
    private UUID cooperativeId;
    
    @Schema(description = "Full name", example = "Marie Claire Uwera")
    private String fullName;
    
    @Schema(description = "Phone number", example = "+250788987654")
    private String phoneNumber;
    
    @Schema(description = "Physical address", example = "Kigali, Kicukiro")
    private String address;
    
    @Schema(description = "Timestamp when customer was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
}