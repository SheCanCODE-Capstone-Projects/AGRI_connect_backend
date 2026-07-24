package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.ProductResponse;
import com.scc.Agriconnect.mapper.ProductMapper;
import com.scc.Agriconnect.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/cooperatives/{cooperativeId}/products")
@RequiredArgsConstructor
@Tag(name = "Public Product Browsing", description = "Public endpoints for browsing cooperative products. No authentication, login, or registration required.")
public class PublicProductController {

    private final ProductService productService;

    @Operation(
            summary = "Browse visible products for a cooperative",
            description = "Public endpoint — no authentication required. Returns only products marked VISIBLE, for customers to browse before buying."
    )
    @GetMapping
    public ResponseEntity<List<ProductResponse>> browseProducts(
            @Parameter(description = "Cooperative UUID")
            @PathVariable UUID cooperativeId) {

        List<ProductResponse> products = productService.listPublicProductsByCooperative(cooperativeId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();

        return ResponseEntity.ok(products);
    }
}