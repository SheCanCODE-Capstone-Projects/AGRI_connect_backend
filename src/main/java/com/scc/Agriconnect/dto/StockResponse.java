package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.ProductUnit;
import com.scc.Agriconnect.entity.StockType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response containing stock movement details")
public class StockResponse {

    @Schema(description = "Unique identifier of the stock movement record", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID stockId;
    
    @Schema(description = "UUID of the product", example = "456e7890-e12b-34d5-a678-901234567890")
    private UUID productId;
    
    @Schema(description = "Name of the product", example = "Organic Coffee Beans")
    private String productName;
    
    @Schema(description = "Product category", example = "Coffee")
    private String category;
    
    @Schema(description = "Quantity moved (positive for IN, same value for OUT)", example = "25.5")
    private BigDecimal quantity;

    @Schema(description = "Unit of measurement", example = "KG")
    private ProductUnit unit;

    @Schema(description = "Type of movement", example = "IN", allowableValues = {"IN", "OUT", "ADJUSTMENT"})
    private StockType stockType;
    
    @Schema(description = "Date of the movement", example = "2024-01-20")
    private LocalDate stockDate;
    
    @Schema(description = "Full name of the staff member who recorded this movement", example = "John Doe Uwase")
    private String recordedByFullName;
    
    @Schema(description = "Additional notes", example = "Received from local farmers")
    private String notes;
    
    @Schema(description = "Associated sale UUID (if this was an automatic OUT during sale)", example = "789a0123-b45c-67d8-e901-234567890123")
    private UUID saleId;
    
    @Schema(description = "Timestamp when record was created", example = "2024-01-20T09:15:00")
    private LocalDateTime createdAt;
}