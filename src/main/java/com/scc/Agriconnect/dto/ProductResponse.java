package com.scc.Agriconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Response containing product information with current stock level")
public class ProductResponse {
    
    @Schema(description = "Unique identifier of the product", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID productId;
    
    @Schema(description = "Unique identifier of the cooperative owning this product", example = "456e7890-e12b-34d5-a678-901234567890")
    private UUID cooperativeId;
    
    @Schema(description = "Name of the product", example = "Organic Coffee Beans")
    private String name;
    
    @Schema(description = "Product category", example = "Coffee")
    private String category;
    
    @Schema(description = "Detailed description", example = "Premium Arabica coffee beans")
    private String description;
    
    @Schema(description = "Unit price in RWF", example = "5000.00")
    private BigDecimal unitPrice;
    
    @Schema(description = "Storage location", example = "Warehouse A, Shelf 5")
    private String storageLocation;
    
    @Schema(description = "Date received", example = "2024-01-20")
    private LocalDate dateReceived;
    
    @Schema(description = "Current available quantity in stock", example = "150.5")
    private BigDecimal currentStockLevel;
    
    @Schema(description = "Product image URL", example = "https://example.com/images/coffee.jpg")
    private String imageUrl;
    
    @Schema(description = "Product visibility status", example = "VISIBLE", allowableValues = {"VISIBLE", "HIDDEN"})
    private String status;
}