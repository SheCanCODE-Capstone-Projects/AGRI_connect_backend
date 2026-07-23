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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        description = "Create a sale transaction for a product. Automatically decreases stock and calculates total amount. Requires PRESIDENT role."
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
    @PreAuthorize("hasRole('PRESIDENT')")
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
            @Parameter(description = "Filter sales from this date (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Filter sales up to this date (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Pagination parameters (page, size, sort)")
            @PageableDefault(size = 20) Pageable pageable) {
        if (from != null && to != null) {
            return ResponseEntity.ok(saleService.getCooperativeSales(from, to, pageable));
        }
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
        summary = "Void a sale",
        description = "Cancel a previously recorded sale. Reverses the sale's stock deduction via a compensating stock-in movement and marks the sale as voided. Requires PRESIDENT role."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sale voided successfully",
            content = @Content(schema = @Schema(implementation = SaleResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Sale already voided"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions or access denied"),
        @ApiResponse(responseCode = "404", description = "Sale not found")
    })
    @PreAuthorize("hasRole('PRESIDENT')")
    @PostMapping("/{saleId}/void")
    public ResponseEntity<SaleResponse> voidSale(
            @Parameter(description = "Sale UUID")
            @PathVariable UUID saleId) {
        return ResponseEntity.ok(saleService.voidSale(saleId));
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
    public ResponseEntity<BigDecimal> totalRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from != null && to != null) {
            return ResponseEntity.ok(saleService.getRevenue(from, to));
        }
        return ResponseEntity.ok(saleService.getTotalRevenue());
    }
}
