package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.ProductUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Request payload for creating or updating a product")
public class ProductRequest {

    @Schema(description = "Name of the product", example = "Organic Coffee Beans", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name is required")
    private String name;

    @Schema(description = "Product category", example = "Coffee")
    private String category;

    @Schema(description = "Detailed description of the product", example = "Premium Arabica coffee beans from high-altitude farms")
    private String description;

    @Schema(description = "Unit price of the product in RWF", example = "5000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;

    @Schema(description = "Unit of measurement", example = "KG")
    @NotNull(message = "Unit of measurement is required")
    private ProductUnit unit;

    @Schema(description = "Stock level at or below which the product needs reordering", example = "10")
    @DecimalMin(value = "0.0", message = "Reorder threshold cannot be negative")
    private BigDecimal reorderThreshold;

    @Schema(description = "Location where product is stored", example = "Warehouse A, Shelf 5")
    private String storageLocation;

    @Schema(description = "Date when product was received", example = "2024-01-20")
    private LocalDate dateReceived;

    @Schema(description = "URL to product image", example = "https://example.com/images/coffee.jpg")
    private String imageUrl;
}