package com.scc.Agriconnect.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String category;

    private String description;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;

    private String storageLocation;

    private LocalDate dateReceived;

    private String imageUrl;
}