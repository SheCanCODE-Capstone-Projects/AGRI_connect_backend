package com.scc.Agriconnect.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private UUID productId;
    private UUID cooperativeId;
    private String name;
    private String category;
    private String description;
    private BigDecimal unitPrice;
    private String storageLocation;
    private LocalDate dateReceived;
    private BigDecimal currentStockLevel;
    private String imageUrl;
    private String status;
}