package com.scc.Agriconnect.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerResponse {
    private UUID customerId;
    private UUID cooperativeId;
    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
}