package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.ProductRequest;
import com.scc.Agriconnect.dto.ProductResponse;
import com.scc.Agriconnect.mapper.ProductMapper;
import com.scc.Agriconnect.service.ProductService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cooperative/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "Manage cooperative products. Create, update products and control their visibility. List products for the authenticated user's cooperative.")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Create a new product",
            description = "Add a new product to the cooperative catalog. Requires PRESIDENT or STAFF role. Product will be visible by default."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ProductMapper.toResponse(productService.create(request)));
    }

    @Operation(
            summary = "Update product details",
            description = "Update an existing product's information. Requires PRESIDENT or STAFF role."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> update(
            @Parameter(description = "Product UUID")
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ProductMapper.toResponse(productService.update(productId, request)));
    }

    @Operation(
            summary = "Set product visibility",
            description = "Show or hide a product from the catalog. Hidden products are not available for sale. Requires PRESIDENT or STAFF role."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product visibility updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PatchMapping("/{productId}/visibility")
    public ResponseEntity<ProductResponse> setVisibility(
            @Parameter(description = "Product UUID")
            @PathVariable UUID productId,
            @Parameter(description = "Set to true to show product, false to hide")
            @RequestParam boolean visible) {
        return ResponseEntity.ok(ProductMapper.toResponse(productService.setVisibility(productId, visible)));
    }

    @Operation(
            summary = "Delete a product",
            description = "Permanently delete a product. Only allowed if the product has no stock history. Requires PRESIDENT or STAFF role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Product has stock history and cannot be deleted"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Product UUID")
            @PathVariable UUID productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "List all products",
            description = "Get all products belonging to the authenticated user's cooperative. Includes current stock levels."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully"
            ),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> products = productService.listForCurrentCooperative()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(products);
    }
}