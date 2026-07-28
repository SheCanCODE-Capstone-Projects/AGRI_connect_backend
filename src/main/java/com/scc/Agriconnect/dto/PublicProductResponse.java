package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.ProductUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Public-facing product listing with cooperative contact details, for buyers browsing without an account")
public class PublicProductResponse {

    @Schema(description = "Unique identifier of the product", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID productId;

    @Schema(description = "Name of the product", example = "Organic Coffee Beans")
    private String name;

    @Schema(description = "Product category", example = "Coffee")
    private String category;

    @Schema(description = "Detailed description", example = "Premium Arabica coffee beans")
    private String description;

    @Schema(description = "Unit price in RWF", example = "5000.00")
    private BigDecimal unitPrice;

    @Schema(description = "Unit of measurement", example = "KG")
    private ProductUnit unit;

    @Schema(description = "Product image URL", example = "https://example.com/images/coffee.jpg")
    private String imageUrl;

    @Schema(description = "Unique identifier of the cooperative selling this product", example = "456e7890-e12b-34d5-a678-901234567890")
    private UUID cooperativeId;

    @Schema(description = "Name of the cooperative selling this product", example = "Kigali Agricultural Cooperative")
    private String cooperativeName;

    @Schema(description = "Province where the cooperative is located", example = "Kigali City")
    private String province;

    @Schema(description = "District where the cooperative is located", example = "Gasabo")
    private String district;

    @Schema(description = "Sector where the cooperative is located", example = "Remera")
    private String sector;

    @Schema(description = "Contact information to reach the cooperative directly", example = "+250788000000")
    private String contactInfo;
}
