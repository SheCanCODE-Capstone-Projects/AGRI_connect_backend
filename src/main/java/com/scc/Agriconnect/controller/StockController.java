package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.StockRequest;
import com.scc.Agriconnect.dto.StockResponse;
import com.scc.Agriconnect.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/cooperative/stock")
@RequiredArgsConstructor
@Tag(name = "Stock Management", description = "Manage product inventory. Track stock movements (IN/OUT), view stock history, and check current stock levels.")
@SecurityRequirement(name = "bearerAuth")
public class StockController {

    private final StockService stockService;

    @Operation(
        summary = "Record stock movement",
        description = "Register a stock IN (receiving), OUT (dispatch/wastage), or ADJUSTMENT (correction, e.g. spoilage/loss) transaction. Stock OUT is automatically recorded during sales. Requires PRESIDENT role."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stock movement recorded successfully",
            content = @Content(schema = @Schema(implementation = StockResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient stock for OUT/ADJUSTMENT movement"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PreAuthorize("hasRole('PRESIDENT')")
    @PostMapping
    public ResponseEntity<StockResponse> recordMovement(@Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.recordStockMovement(request));
    }

    @Operation(
        summary = "Get product stock history",
        description = "View all stock movements (IN/OUT) for a specific product with pagination"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stock history retrieved successfully"
        ),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<StockResponse>> productHistory(
            @Parameter(description = "Product UUID") 
            @PathVariable UUID productId,
            @Parameter(description = "Pagination parameters (page, size, sort)") 
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockService.getProductStockHistory(productId, pageable));
    }

    @Operation(
        summary = "Get cooperative stock history",
        description = "View all stock movements for all products in the authenticated user's cooperative with pagination"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stock history retrieved successfully"
        ),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<Page<StockResponse>> cooperativeHistory(
            @Parameter(description = "Filter movements from this date (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Filter movements up to this date (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Pagination parameters (page, size, sort)")
            @PageableDefault(size = 20) Pageable pageable) {
        if (from != null && to != null) {
            return ResponseEntity.ok(stockService.getCooperativeStockHistory(from, to, pageable));
        }
        return ResponseEntity.ok(stockService.getCooperativeStockHistory(pageable));
    }

    @Operation(
        summary = "Get current stock level",
        description = "Get the current available quantity for a specific product (sum of all IN movements minus OUT movements)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Current stock level retrieved successfully",
            content = @Content(schema = @Schema(implementation = BigDecimal.class))
        ),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/product/{productId}/current")
    public ResponseEntity<BigDecimal> currentStock(
            @Parameter(description = "Product UUID") 
            @PathVariable UUID productId) {
        return ResponseEntity.ok(stockService.getCurrentStock(productId));
    }
}
