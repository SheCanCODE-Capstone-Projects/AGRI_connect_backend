package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.StockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {

    private UUID stockId;
    private UUID productId;
    private String productName;
    private String category;
    private BigDecimal quantity;
    private StockType stockType;
    private LocalDate stockDate;
    private String recordedByFullName;
    private String notes;
    private UUID saleId;
    private LocalDateTime createdAt;
}