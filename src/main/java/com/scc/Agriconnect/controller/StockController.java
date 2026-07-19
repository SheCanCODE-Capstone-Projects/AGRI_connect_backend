package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.StockRequest;
import com.scc.Agriconnect.dto.StockResponse;
import com.scc.Agriconnect.service.StockService;
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
@RequestMapping("/api/cooperative/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PreAuthorize("hasAnyRole('PRESIDENT', 'STAFF')")
    @PostMapping
    public ResponseEntity<StockResponse> recordMovement(@Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.recordStockMovement(request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<StockResponse>> productHistory(@PathVariable Long productId,
                                                                @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockService.getProductStockHistory(productId, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<StockResponse>> cooperativeHistory(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockService.getCooperativeStockHistory(pageable));
    }

    @GetMapping("/product/{productId}/current")
    public ResponseEntity<BigDecimal> currentStock(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getCurrentStock(productId));
    }
}
