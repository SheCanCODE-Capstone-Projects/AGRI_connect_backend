package com.scc.Agriconnect.controller;

import com.scc.Agriconnect.dto.CustomerRequest;
import com.scc.Agriconnect.dto.CustomerResponse;
import com.scc.Agriconnect.mapper.CustomerMapper;
import com.scc.Agriconnect.service.CustomerService;
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
@RequestMapping("/api/cooperative/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "Manage cooperative customers. Create, update and list customers who purchase products from the cooperative.")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Create a new customer",
            description = "Register a new customer for the cooperative. Requires PRESIDENT, ACCOUNTANT, or STOCKMANAGER role."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasAnyRole('PRESIDENT', 'ACCOUNTANT', 'STOCKMANAGER')")
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(CustomerMapper.toResponse(customerService.create(request)));
    }

    @Operation(
            summary = "Update customer details",
            description = "Update an existing customer's information. Requires PRESIDENT, ACCOUNTANT, or STOCKMANAGER role."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer updated successfully",
                    content = @Content(schema = @Schema(implementation = CustomerResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasAnyRole('PRESIDENT', 'ACCOUNTANT', 'STOCKMANAGER')")
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> update(
            @Parameter(description = "Customer UUID")
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(CustomerMapper.toResponse(customerService.update(customerId, request)));
    }

    @Operation(
            summary = "Delete a customer",
            description = "Permanently delete a customer record. Requires PRESIDENT, ACCOUNTANT, or STOCKMANAGER role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasAnyRole('PRESIDENT', 'ACCOUNTANT', 'STOCKMANAGER')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Customer UUID")
            @PathVariable UUID customerId) {
        customerService.delete(customerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "List all customers",
            description = "Retrieve all customers for the authenticated user's cooperative"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customers retrieved successfully"
            ),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> list() {
        List<CustomerResponse> customers = customerService.listForCurrentCooperative()
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
        return ResponseEntity.ok(customers);
    }
}