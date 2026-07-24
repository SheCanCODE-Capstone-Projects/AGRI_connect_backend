package com.scc.Agriconnect.service;

import com.scc.Agriconnect.dto.CustomerRequest;
import com.scc.Agriconnect.entity.Cooperative;
import com.scc.Agriconnect.entity.Customer;
import com.scc.Agriconnect.entity.User;
import com.scc.Agriconnect.repository.CustomerRepository;
import com.scc.Agriconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Transactional
    public Customer create(CustomerRequest request) {
        Cooperative cooperative = getCurrentUserCooperative();

        Customer customer = Customer.builder()
                .cooperative(cooperative)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer update(UUID customerId, CustomerRequest request) {
        Customer customer = getOwnedCustomerOrThrow(customerId);

        customer.setFullName(request.getFullName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());

        return customerRepository.save(customer);
    }

    @Transactional
    public void delete(UUID customerId) {
        Customer customer = getOwnedCustomerOrThrow(customerId);
        customerRepository.delete(customer);
    }

    public List<Customer> listForCurrentCooperative() {
        Cooperative cooperative = getCurrentUserCooperative();
        return customerRepository.findByCooperative_CooperativeId(cooperative.getCooperativeId());
    }

    private Customer getOwnedCustomerOrThrow(UUID customerId) {
        Cooperative cooperative = getCurrentUserCooperative();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        if (!customer.getCooperative().getCooperativeId().equals(cooperative.getCooperativeId())) {
            throw new IllegalStateException("You do not have access to this customer");
        }
        return customer;
    }

    private Cooperative getCurrentUserCooperative() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User freshUser = userRepository.findById(currentUser.getUserId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        Cooperative cooperative = freshUser.getCooperative();
        if (cooperative == null) {
            throw new IllegalStateException("Only cooperative members can manage customers");
        }
        return cooperative;
    }
}