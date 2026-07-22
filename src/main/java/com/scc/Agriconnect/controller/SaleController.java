package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.SaleRequest;
import com.scc.Agriconnect.dto.SaleResponse;
import com.scc.Agriconnect.service.SaleService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/cooperative/sales")
@RequiredArgsConstructor
@Tag(name = "Sales Management", description = "Record and track product sales. Automatically updates stock levels and calculates revenue.")
@SecurityRequirement(name = "bearerAuth")
public class SaleController {

    private final SaleService saleService;

    @Operation(
        summary = "Record a new sale",
        description = "Create a sale transaction for a product. Automatically decreases stock and calculates total amount. Requires PRESIDENT or STAFF role."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sale recorded successfully",
            content = @Content(schema = @Schema(implementation = SaleResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient stock"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "404", description = "Product or customer not found")
    })
    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PostMapping
    public ResponseEntity<SaleResponse> record(@Valid @RequestBody SaleRequest request) {
        return ResponseEntity.ok(saleService.recordSale(request));
    }

    @Operation(
        summary = "List all sales",
        description = "Get paginated sales for the authenticated user's cooperative. Includes product and customer details."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sales retrieved successfully"
        ),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<Page<SaleResponse>> list(
            @Parameter(description = "Pagination parameters (page, size, sort)") 
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(saleService.getCooperativeSales(pageable));
    }

    @Operation(
        summary = "Get sale by ID",
        description = "Retrieve detailed information about a specific sale transaction"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sale found",
            content = @Content(schema = @Schema(implementation = SaleResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Sale not found")
    })
    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponse> get(
            @Parameter(description = "Sale UUID") 
            @PathVariable UUID saleId) {
        return ResponseEntity.ok(saleService.getSaleById(saleId));
    }

    @Operation(
        summary = "Get total revenue",
        description = "Calculate total revenue from all sales for the authenticated user's cooperative"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Total revenue calculated successfully",
            content = @Content(schema = @Schema(implementation = BigDecimal.class))
        ),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> totalRevenue() {
        return ResponseEntity.ok(saleService.getTotalRevenue());
    }
}
