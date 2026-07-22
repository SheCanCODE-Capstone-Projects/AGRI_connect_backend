package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.CustomerResponse;
import com.scc.Agriconnect.entity.Customer;

public class CustomerMapper {

    public static CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .cooperativeId(customer.getCooperative().getCooperativeId())
                .fullName(customer.getFullName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}