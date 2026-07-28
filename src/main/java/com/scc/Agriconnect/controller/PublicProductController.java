package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.PublicProductResponse;
import com.scc.Agriconnect.mapper.ProductMapper;
import com.scc.Agriconnect.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "Public Product Browsing", description = "Public landing-page endpoints for buyers to discover cooperatives and their products. No authentication, login, or registration required.")
public class PublicProductController {

    private final ProductService productService;

    @Operation(
            summary = "Browse all products across all cooperatives",
            description = "Public landing-page endpoint — no authentication required. Returns every in-stock, visible product from every approved cooperative on the platform, " +
                    "including the cooperative's name, location, and contact info so buyers can reach out directly."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/products")
    public ResponseEntity<List<PublicProductResponse>> browseAllProducts() {
        List<PublicProductResponse> products = productService.listAllPublicProducts()
                .stream()
                .map(ProductMapper::toPublicResponse)
                .toList();

        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Browse a specific cooperative's products",
            description = "Public endpoint — no authentication required. Returns every in-stock, visible product for one approved cooperative, " +
                    "including that cooperative's name, location, and contact info. Used after a buyer has found a cooperative on the landing page."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/cooperatives/{cooperativeId}/products")
    public ResponseEntity<List<PublicProductResponse>> browseProductsByCooperative(
            @Parameter(description = "Cooperative UUID")
            @PathVariable UUID cooperativeId) {

        List<PublicProductResponse> products = productService.listPublicProductsByCooperative(cooperativeId)
                .stream()
                .map(ProductMapper::toPublicResponse)
                .toList();

        return ResponseEntity.ok(products);
    }
}