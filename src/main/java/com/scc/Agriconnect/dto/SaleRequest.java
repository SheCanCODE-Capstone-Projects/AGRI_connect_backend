package com.scc.Agriconnect.dto;

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
public class SaleRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID customerId;

    @NotNull(message = "Quantity sold is required")
    @Min(value = 1, message = "Quantity sold must be greater than 0")
    private BigDecimal quantitySold;

    private LocalDate saleDate;

    private String notes;
}