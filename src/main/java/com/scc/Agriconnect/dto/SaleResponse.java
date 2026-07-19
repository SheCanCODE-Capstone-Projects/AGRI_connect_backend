package com.scc.Agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {

    private Long saleId;
    private Long productId;
    private String productName;
    private String category;
    private Long cooperativeId;
    private String cooperativeName;
    private Long customerId;
    private String customerName;
    private BigDecimal quantitySold;
    private BigDecimal totalAmount;
    private LocalDate saleDate;
    private String recordedByFullName;
    private String notes;
    private LocalDateTime createdAt;
}