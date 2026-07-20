package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.StockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Stock type is required")
    private StockType stockType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    private LocalDate stockDate;

    private String notes;

    private Long saleId;
}