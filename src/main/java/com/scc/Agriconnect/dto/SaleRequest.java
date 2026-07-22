package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for recording a product sale")
public class SaleRequest {

    @Schema(description = "UUID of the product being sold", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Schema(description = "UUID of the customer making the purchase (optional — leave null for anonymous/walk-in)", example = "789a0123-b45c-67d8-e901-234567890123")
    private UUID customerId;

    @Schema(description = "Quantity sold (must be greater than 0 and not exceed current stock)", example = "10.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Quantity sold is required")
    @Min(value = 1, message = "Quantity sold must be greater than 0")
    private BigDecimal quantitySold;

    @Schema(description = "Date of the sale (defaults to today if not provided)", example = "2024-01-22")
    private LocalDate saleDate;

    @Schema(description = "Optional notes about this sale", example = "Bulk order for NGO event")
    private String notes;
}