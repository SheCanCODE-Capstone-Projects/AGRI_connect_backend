package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.StockType;
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
public class StockResponse {

    private Long stockId;
    private Long productId;
    private String productName;
    private String category;
    private BigDecimal quantity;
    private StockType stockType;
    private LocalDate stockDate;
    private String recordedByFullName;
    private String notes;
    private Long saleId;
    private LocalDateTime createdAt;
}