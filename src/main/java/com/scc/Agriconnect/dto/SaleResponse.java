package com.scc.Agriconnect.dto;

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
@Schema(description = "Response containing full sale transaction details")
public class SaleResponse {

    @Schema(description = "Unique identifier of the sale", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID saleId;
    
    @Schema(description = "UUID of the product sold", example = "456e7890-e12b-34d5-a678-901234567890")
    private UUID productId;
    
    @Schema(description = "Name of the product sold", example = "Organic Coffee Beans")
    private String productName;
    
    @Schema(description = "Product category", example = "Coffee")
    private String category;
    
    @Schema(description = "UUID of the cooperative that made the sale", example = "789a0123-b45c-67d8-e901-234567890123")
    private UUID cooperativeId;
    
    @Schema(description = "Name of the cooperative", example = "Kigali Agricultural Cooperative")
    private String cooperativeName;
    
    @Schema(description = "UUID of the customer (null if anonymous)", example = "abc12345-def6-7890-ghij-123456789012")
    private UUID customerId;
    
    @Schema(description = "Name of the customer (null if anonymous)", example = "Marie Claire Uwera")
    private String customerName;
    
    @Schema(description = "Quantity sold", example = "10.5")
    private BigDecimal quantitySold;
    
    @Schema(description = "Total amount (quantity × unit price)", example = "52500.00")
    private BigDecimal totalAmount;
    
    @Schema(description = "Date of the sale", example = "2024-01-22")
    private LocalDate saleDate;
    
    @Schema(description = "Full name of the staff member who recorded the sale", example = "John Doe Uwase")
    private String recordedByFullName;
    
    @Schema(description = "Additional notes", example = "Bulk order for NGO event")
    private String notes;
    
    @Schema(description = "Timestamp when sale record was created", example = "2024-01-22T14:30:00")
    private LocalDateTime createdAt;
}