package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCooperative_CooperativeId(Long cooperativeId);
}