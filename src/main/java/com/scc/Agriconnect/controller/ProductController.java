package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.ProductRequest;
import com.scc.Agriconnect.dto.ProductResponse;
import com.scc.Agriconnect.mapper.ProductMapper;
import com.scc.Agriconnect.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cooperative/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ProductMapper.toResponse(productService.create(request)));
    }

    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long productId,
                                                   @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ProductMapper.toResponse(productService.update(productId, request)));
    }

    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PatchMapping("/{productId}/visibility")
    public ResponseEntity<ProductResponse> setVisibility(@PathVariable Long productId,
                                                          @RequestParam boolean visible) {
        return ResponseEntity.ok(ProductMapper.toResponse(productService.setVisibility(productId, visible)));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> products = productService.listForCurrentCooperative()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(products);
    }
}