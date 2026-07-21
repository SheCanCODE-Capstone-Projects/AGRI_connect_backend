package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.SaleRequest;
import com.scc.Agriconnect.dto.SaleResponse;
import com.scc.Agriconnect.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cooperative/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PostMapping
    public ResponseEntity<SaleResponse> record(@Valid @RequestBody SaleRequest request) {
        return ResponseEntity.ok(saleService.recordSale(request));
    }

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> list(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(saleService.getCooperativeSales(pageable));
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<SaleResponse> get(@PathVariable Long saleId) {
        return ResponseEntity.ok(saleService.getSaleById(saleId));
    }

    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> totalRevenue() {
        return ResponseEntity.ok(saleService.getTotalRevenue());
    }
}
