package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findByCooperative_CooperativeId(UUID cooperativeId);
}