package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.CustomerRequest;
import com.scc.Agriconnect.dto.CustomerResponse;
import com.scc.Agriconnect.mapper.CustomerMapper;
import com.scc.Agriconnect.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cooperative/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PreAuthorize("hasAnyRole('PRESIDENT', 'ACCOUNTANT', 'STOCKMANAGER')")
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(CustomerMapper.toResponse(customerService.create(request)));
    }

    @PreAuthorize("hasAnyRole('PRESIDENT', 'ACCOUNTANT', 'STOCKMANAGER')")
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long customerId,
                                                    @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(CustomerMapper.toResponse(customerService.update(customerId, request)));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> list() {
        List<CustomerResponse> customers = customerService.listForCurrentCooperative()
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
        return ResponseEntity.ok(customers);
    }
}