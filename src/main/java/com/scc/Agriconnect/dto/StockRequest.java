package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.StockType;
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
@Schema(description = "Request payload for recording a stock movement (IN or OUT)")
public class StockRequest {

    @Schema(description = "UUID of the product", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Schema(description = "Type of stock movement", example = "IN", allowableValues = {"IN", "OUT"}, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Stock type is required")
    private StockType stockType;

    @Schema(description = "Quantity being added (IN) or removed (OUT)", example = "25.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @Schema(description = "Date of the stock movement (defaults to today if not provided)", example = "2024-01-20")
    private LocalDate stockDate;

    @Schema(description = "Optional notes explaining the movement", example = "Received from local farmers")
    private String notes;

    @Schema(description = "UUID of associated sale (only for automatic OUT movements during sales)", example = "456e7890-e12b-34d5-a678-901234567890")
    private UUID saleId;
}