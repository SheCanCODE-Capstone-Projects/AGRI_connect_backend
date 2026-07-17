package com.scc.Agriconnect.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {
    private Long customerId;
    private Long cooperativeId;
    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
}